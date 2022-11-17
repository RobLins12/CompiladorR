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
            currToken = scanner.nextToken();
            if (currToken.getLexema().equals("}")) {
            }else{
                throw new RuntimeException("Bloco mal formatado");
            } 
        }else{
            throw new RuntimeException("Bloco mal formatado");
        }
    }

    private void comando() {
        currToken = scanner.nextToken();
        if(currToken.getLexema().equals("if")){
            currToken = scanner.nextToken();
            if (currToken.getLexema().equals("(")) {
                expr_relacional();
                if (currToken.getLexema().equals(")")) {
                    comando();
                    //currToken = scanner.nextToken();
                    //if (currToken.getLexema().equals("else")) {
                       // comando();
                    //}
                } else {
                    throw new RuntimeException("Condicional mal formatado");
                }
            }else{
                throw new RuntimeException("Condicional mal formatado");
            }
        }
        else if(currToken.getLexema().equals("while")){
            currToken = scanner.nextToken();
            if (currToken.getLexema().equals("(")) {
                expr_relacional();
                if (currToken.getLexema().equals(")")) {
                    comando();
                } else {
                    throw new RuntimeException("Iteração mal formatado");
                }
            } else {
                throw new RuntimeException("Iteração mal formatado");
            }
        }
        else if(currToken.getTipo() == 3) {
            currToken = scanner.nextToken();
            if (currToken.getLexema().equals("=")) {
                expr_arit();
                currToken = scanner.nextToken();
                if (currToken.getLexema().equals(";")) {
                    
                } else {
                    throw new RuntimeException("Atribuição mal formatado");
                }
            } else {
                throw new RuntimeException("Atribuição mal formatado");
            }
        }
        else if(currToken.getLexema().equals("{")){
            decl_var();
            comando();
            currToken = scanner.nextToken();
            if (currToken.getLexema().equals("}")) {
            }else{
                throw new RuntimeException("Bloco mal formatado");
            } 
        }
        else{
            throw new RuntimeException("Comando mal formatado");
        }
    }

    public void expr_relacional() {
        expr_arit();
        if (currToken.getTipo() == 4) {
            expr_arit();
        }else{
            throw new RuntimeException("Expr relacional mal formatada");
        }
    }

    public void expr_arit() {
        termo();
        EA();
    }

    public void EA() {
        if (currToken.getLexema().equals("+")) {
            termo();
            EA();
        }
        if (currToken.getLexema().equals("-")) {
            termo();
            EA();
        }
        else{
            return;
        }
    }

    public void termo() {
        fator();
        currToken = scanner.nextToken();
        TL();
    }

    public void TL() {
        if (currToken.getLexema().equals("*")) {
            fator();
            TL();
        }
        if (currToken.getLexema().equals("/")) {
            fator();
            TL();
        }
        else{
        }
    }

    public void fator() {
        currToken = scanner.nextToken();
        if (currToken.getLexema().equals("(")) {
            expr_relacional();
            currToken = scanner.nextToken();
            if (currToken.getLexema().equals(")")) {
            }else{
                throw new RuntimeException("Fator mal formatado");
            }
        }else if (currToken.getTipo() == 0 || currToken.getTipo() == 1 || currToken.getTipo() == 2 || currToken.getTipo() == 3) {
            
        }else{
            throw new RuntimeException("Fator mal formatado");
        }
    }

    public void decl_var() {
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
