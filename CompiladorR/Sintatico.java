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
                        currToken = scanner.nextToken();
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
        if (currToken.getLexema().equals("{")) {
            currToken = scanner.nextToken();
            while (currToken.getTipo() != Token.TIPO_FIM_CODIGO && !currToken.getLexema().equals("}")) {
                if (currToken.getLexema().equals("int")|| 
                currToken.getLexema().equals("float") || 
                currToken.getLexema().equals("char")) 
                {
                   decl_var(); 
                }
                else{
                   comando(); 
                }
            }
            if (currToken.getLexema().equals("}")) {
                currToken = scanner.nextToken();
            }else{
                throw new RuntimeException("Bloco mal formatado");
            } 
        }else{
            throw new RuntimeException("Bloco mal formatado");
        }
    }

    public void comando() {
        if(currToken.getLexema().equals("if")){
            condicional();
        }
        else if(currToken.getLexema().equals("while")){
            iteração();
        }
        else if(currToken.getTipo() == 3 || currToken.getLexema().equals("{")) {
            comandoBasico();
        }
    }

    public void comandoBasico() {
        if (currToken.getTipo() == 3) {
            atribuição();
        } else if(currToken.getLexema().equals("{")) {
            bloco();
        }else{
            throw new RuntimeException("Comando básico inválido");
        }
    }

    public void atribuição() {
        if (currToken.getTipo() == 3) {
            currToken = scanner.nextToken();
            if (currToken.getLexema().equals("=")) {
                currToken = scanner.nextToken();
                expr_arit();
                if (currToken.getLexema().equals(";")) {
                    currToken = scanner.nextToken();
                } else{
                    throw new RuntimeException("Atribuicao inválida"); 
                }
            } else {
                throw new RuntimeException("Atribuicao inválida"); 
            }
        }else{
            throw new RuntimeException("Atribuicao inválida"); 
        }
    }

    public void iteração() {
        currToken = scanner.nextToken();
        if (currToken.getLexema().equals("(")) {
            currToken = scanner.nextToken();
            expr_relacional();
            if (currToken.getLexema().equals(")")) {
                currToken = scanner.nextToken();
            }else{
                throw new RuntimeException("Iteração mal formatada");
            }
        }else{
            throw new RuntimeException("Iteração mal formatada");
        }
    }

    public void condicional() {
        currToken = scanner.nextToken();
        if (currToken.getLexema().equals("(")) {
            currToken = scanner.nextToken();
                expr_relacional();
                if (currToken.getLexema().equals(")")) {
                    currToken = scanner.nextToken();
                    comando();
                    if (currToken.getLexema().equals("else")) {
                        currToken = scanner.nextToken();
                        comando();
                    }
                } else {
                    throw new RuntimeException("Condicional mal formatado");
                }
            }else{
                throw new RuntimeException("Condicional mal formatado");
            }
    }

    public void expr_relacional() {
        expr_arit();
        if (currToken.getTipo() == 4) {
            currToken = scanner.nextToken();
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
        if (currToken.getLexema().equals("+") || currToken.getLexema().equals("-")) {
            currToken = scanner.nextToken();
            termo();
            EA();
        }
    }

    public void termo() {
        fator();
        TL();
    }

    public void TL() {
        if (currToken.getLexema().equals("*") || currToken.getLexema().equals("/")) {
            currToken = scanner.nextToken();
            fator();
            TL();
        }
    }

    public void fator() {
        if (currToken.getLexema().equals("(")) {
            currToken = scanner.nextToken();
            expr_relacional();
            if (currToken.getLexema().equals(")")) {
                currToken = scanner.nextToken();
            }else{
                throw new RuntimeException("Fator mal formatado");
            }
        }else if (currToken.getTipo() == 0 || currToken.getTipo() == 1 || currToken.getTipo() == 2 || currToken.getTipo() == 3) {
            currToken = scanner.nextToken();
        }else{
            throw new RuntimeException("Fator mal formatado");
        }
    }

    public void decl_var() {
        if ((currToken.getLexema().equals("int")||
        currToken.getLexema().equals("float") ||
        currToken.getLexema().equals("char") )) {
            currToken = scanner.nextToken();   
            if (currToken.getTipo() == Token.TIPO_IDENTIFICADOR) {
                currToken = scanner.nextToken();  
                if (currToken.getLexema().equals(";")) {
                    currToken = scanner.nextToken();  
                }else{
                    throw new RuntimeException("Declaração de variável mal formatada");
                }
            } 
            else{
                throw new RuntimeException("Declaração de variável mal formatada");
            }
        }else{
            throw new RuntimeException("Declaração de variável mal formatada");
        }
    }
}
