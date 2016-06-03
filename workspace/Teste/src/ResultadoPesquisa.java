public class ResultadoPesquisa {
	private String nrLinha, nomeColuna, descricaoErro;
		
	public ResultadoPesquisa(String nrLinha, String nomeColuna, String descricaoErro) {
        this.nrLinha = nrLinha;
        this.nomeColuna = nomeColuna;
        this.descricaoErro = descricaoErro;
    }
	
	public String getNrLinha() {
        return nrLinha;
    }

    public void setNrLinha(String nrLinha) {
        this.nrLinha = nrLinha;
    }
    
    public String getNomeColuna() {
        return nomeColuna;
    }

    public void setNomeColuna(String nomeColuna) {
        this.nomeColuna = nomeColuna;
    }
    
    public String getDescricaoErro() {
        return descricaoErro;
    }

    public void setDescricaoErro(String descricaoErro) {
        this.descricaoErro = descricaoErro;
    }
	
}
