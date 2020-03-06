/** 
      * IMPORTAR DADOS DO EXCEL  
      *  
      * @param item 
      * @param definicao 
      * @return List<ModalConverterEntity> 
      */ 
     public List<ModalConverterEntity> converterEntityMigracao(FileUploadHistory item, Definicao definicao) { 
            
           ColumnDef[] colunas = definicao.getColunas();               
           XSSFWorkbook workbook  = null;            

           try { 
                  String nomeLocal = System.getProperty("user.dir") + "\\"+  item.getFileGlobal().getOriginalFilename(); 
                  InputStream is = new FileInputStream(nomeLocal); 
                  logger.info("Coletando dados do Excel..."); 
                  workbook = new XSSFWorkbook(is); // PROCESSAMENTO LENTO 
                  item.setArquivoExcel(workbook); 

            } catch (IOException e1) { 
                logger.error("ERRO   " + e1.getMessage());   

            }                    
            UploadFileResponse.setFlagArquivosEmprocessamento( 
            UploadFileResponse.getFlagArquivosEmprocessamento() + 1L);         
            XSSFSheet worksheet = workbook.getSheetAt(0); 
            XSSFRow rowHeader = worksheet.getRow(0);        
            ArrayList<ModalConverterEntity> modalConverterList = new ArrayList<ModalConverterEntity>();              

            for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++)  {             
                 XSSFRow row = worksheet.getRow(i);     
                 int cont = 0; 
                 for(ColumnDef coluna: colunas) {                          
                        ModalConverterEntity modalConverter = new ModalConverterEntity(); 
                        try { 
                            if(row.getCell(cont) != null) modalConverter.setValorColuna(row.getCell(cont).toString()); 
                            if(rowHeader.getCell(cont) != null) modalConverter.setNomeColuna(rowHeader.getCell(cont).toString()); 
                            if(coluna.getColunaDbMig() != null) { modalConverter.setColunaDbMig(coluna.getColunaDbMig()); }                          
                            modalConverterList.add(modalConverter);                                 

                        } catch (Exception e) { 
                            logger.error("Erro: O arquivo está corrompido " + e.getMessage());   
                            continue; 
                        } 
                        cont++; 
                 }                                                                                                      
              }      
          
             boolean removeFileStored = new File(item.getFileGlobal().getOriginalFilename()).delete();   
             return modalConverterList; 
     }      
      
    /** 
     * IMPORTAR DADOS DO CSV 
     *   
     * @param file 
     * @param definicao 
     * @return List<ModalConverterEntity> 
     */ 

    public List<ModalConverterEntity> convertCsvToEntityMigracao(MultipartFile file, Definicao definicao) { 
        ColumnDef[] colunas = definicao.getColunas();   
        ArrayList<ModalConverterEntity> modalConverterList = new ArrayList<ModalConverterEntity>();   
         
        try {  
            /**** Obtem a instancia de CSVReader  e especifica o delimitador para ser usado ****/            
            String[] nextLine,  nextLineHeader;             
             Reader reader2 = new InputStreamReader(file.getInputStream());   
             Reader reader3 = new InputStreamReader(file.getInputStream());             
             CSVReader readerHeaderCsv = new CSVReaderBuilder(reader3).build();               
             CSVReader reader = new CSVReaderBuilder(reader2).withSkipLines(1).build();        
             List<String> headeritens  = new ArrayList<String>();              
             while((nextLineHeader = readerHeaderCsv.readNext()) != null) { 
                String headerCsv = nextLineHeader[0];              
                headeritens = Arrays.asList(headerCsv.split("\\s*;\\s*")); 
                break; 
             }                                                             
            while((nextLine = reader.readNext()) != null) { 
                  String itensLinha = nextLine[0];                 
                  List<String> items = Arrays.asList(itensLinha.split("\\s*;\\s*"));                   
                  for(int i=0; i < items.size(); i++) { 
                      try { 
                          for(ColumnDef col : colunas ) { 
                              if(col.getNome().equals(headeritens.get(i))) { 
                                  ModalConverterEntity modalConverter = new ModalConverterEntity(); 
                                  modalConverter.setValorColuna(items.get(i).toString()); 
                                  modalConverter.setColunaDbMig(col.getColunaDbMig()); 
                                  modalConverter.setNomeColuna(headeritens.get(i));                         
                                  modalConverterList.add(modalConverter); 
                              } 
                          }                           
                      }catch(Exception e) { 
                          logger.error("ERRO ao converter o dado " + items.get(i) + " da linha " + i); 
                      }                    
                  } 
             } 
          } catch(Exception exObj) { 

              logger.error("Exception In convertCsvToXls() Method?=  " + exObj); 
          }        
          boolean removeFileStored = new File(file.getOriginalFilename()).delete();
          return modalConverterList;       
    } 
