package CompiladorR;

public class Sintatico {
    
    private Lexico scanner;
    private Token currToken;

    public Sintatico(Lexico scanner){
        this.scanner = scanner;
    }

    public void programa(){
        currToken = scanner.nextToken();
        if (currToken.getLexema().equals("int")) {
            currToken = scanner.nextToken();
            if (currToken.getLexema().equals("main")) {
                currToken = scanner.nextToken();
                if (currToken.getLexema().equals("(")) {
                    currToken = scanner.nextToken();
                    if (currToken.getLexema().equals(")")) {
                        bloco();
                    } else {
                        throw new RuntimeException("Erro na iniciação do programa Esperado: )"); 
                    }
                } else {
                    throw new RuntimeException("Erro na iniciação do programa Esperado: (");
                }
            } else {
                throw new RuntimeException("Erro na iniciação do programa Esperado: main");
            }
        }else{
            throw new RuntimeException("Erro na iniciação do programa Esperado: int");
        }
    }

    public void bloco() {
        currToken = scanner.nextToken();
        if (currToken.getLexema().equals("{")) {
            decl_var();
            comando();
            
        }
    }

    private void comando() {
        currToken = scanner.nextToken();
        if(currToken.getLexema().equals("if")){

        }
        else if (currToken.getLexema().equals("while")){
            
        }
    }

    private void decl_var() {
        currToken = scanner.nextToken();
        if (currToken.getLexema().equals("int")| currToken.getLexema().equals("float") | currToken.getLexema().equals("char")) {
            currToken = scanner.nextToken();
            if (currToken.getTipo() == 3) {
                currToken = scanner.nextToken();
                if (currToken.getLexema().equals(";")) {
                }
                else{
                    throw new RuntimeException("Declaração de variavel mal formatada");
                }
            } else {
                throw new RuntimeException("Declaração de variavel mal formatada");
            }
        }else{
            throw new RuntimeException("Declaração de variavel mal formatada");
        }
    }
}
