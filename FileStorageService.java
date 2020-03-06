   public List<String> retornaColunasArquivo(MultipartFile file) {              
       XSSFWorkbook workbook = null;         
             try { 
                 workbook = new XSSFWorkbook(file.getInputStream()); 

             } catch (IOException e) { 
                  new IOException("ERRO  ao ler o arquivo " + e.getMessage());              
             }            
             XSSFSheet worksheet = workbook.getSheetAt(0); 
             XSSFRow rowCabecalho = worksheet.getRow(0);                     
             List<String> colunasArquivo = new ArrayList<String>();             
             int count =  rowCabecalho.getRowNum(); 
             
            for(int i =0; i < count; i ++) { 
               colunasArquivo.add(rowCabecalho.getCell(i).toString()); 

            }                    
            return colunasArquivo;                  
   } 
