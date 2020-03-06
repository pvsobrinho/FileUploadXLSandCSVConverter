/** 

     * Converter de CSV para AlunoEnderecoMigracao 

     *  

     * @param item 

     * @param definicao 

     * @return List<AlunoEnderecoMigracao> 

     */ 

    public List<AlunoEnderecoMigracao> convertCsvToAlunoEnderecoMigracao(FileUploadHistory item, Definicao definicao){ 

         

        ConverterUtil converterUtil = new ConverterUtil(); 

        List<ModalConverterEntity> modalConverterList = converterUtil.convertCsvToEntityMigracao(item.getFileGlobal(), definicao); 

        return conversorAlunoEnderecoMigracaoEntity(modalConverterList);                  

    } 

     

    /** 

     * Converter de EXCEL para  AlunoEnderecoMigracao 

     *  

     * @param item 

     * @param definicao 

     * @return List<AlunoEnderecoMigracao> 

     */ 

    public List<AlunoEnderecoMigracao> converterAlunoEnderecoMigracao(FileUploadHistory item, Definicao definicao) { 

         

        ConverterUtil converterUtil = new ConverterUtil(); 

        List<ModalConverterEntity> modalConverterList = converterUtil.converterEntityMigracao(item, definicao); 

        return conversorAlunoEnderecoMigracaoEntity(modalConverterList);        

    }  
