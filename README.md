# Bem vindo ao Jarsed!

O **Jarsed** (**J** ust **A** nother **R** egexp **S** tream **ED** itor) é mais uma editor para o fito, cujo o acrônimo do nome, já deixa bastante claro o propósito de seu uso. Mas, por que mais um, se já temos tantos utilitários como: o SED, o AWK, PowerShell e tantos outros? 

Bem, considerando que na minha estação de trabalho, no trbalho, eu não tenho privilégios para instalar nada, só me restariam duas opções: **Java** ou **PowerShell**, pois, o seus interpretadores já são nativos no ambiente Desktop proficcional. 

# Motivação

Esse projeto nasceu da necessidade de formatar arquivos capturados da web, num processo de automação, feito com **Power Automate**, que verifica as notas fiscais no site da Receita Federal , de modo a checar a integridade dos dados nas Notas fiscais, duranto os processos de pagamentos.

## Simplicidade

A idéia deste projeto é que ele seja estremamente simples, com poucas linhas de código, apenas para ler o arquivo de script **RegExp**, contendo uma linha para cada triade de parâmtros, separados por **|** (pipe), sendo o primeiro uma expressão regular, padrão Java, a segunda o texto a ser substítuído, a  cada ocorrência do primeiro parâmtro e o último um modificados de escopo de atuação da expressão regular, que por hora só tem duas opções: __g__ e __g+__, a primeira indica que a pesquisa pelo padrão, indicado no primeiro parâmetro,  deve ocorrer até encpntrar o primeiro **\n** (fim de linha) no texto. A segunda opção, torna a busca extensível a todo o texto.

## Exemplo de uso

A tabela a seguir demonstra um resumo dos casos de uso, e código a seguir um exemplo de arquivo TXT, em formato UTF-8, com separadores de linha **\n**, contendo

|  Padrão RegExp | Texto a ser substituído | Escopo |
| ---            | ----                    | ---    |
| \r\n           | \n                      |g+      |
| \n\n+          | \n                      |g+      |
| Brasil.*Dados Gerais|                    |g+      |
| Chave de Acesso| ChaveAcesso:            |g+      |
| (ChaveAcesso:) *([0-9 ]*) | $1 "$2"\n    |g+      |
| (N.\*mero)([0-9]*)(Vers.*XML.*) | Numero: "$2"\n$3 |g+|
| Vers.*XML(\[\^\n]\*)\n | VersaoXML: "$1"\n | g+ |
| NFe(.\*)Modelo | Modelo# | g+ |

### Exemplo de arquivo

Vide mais detalhes no comentários do exemplo de script a seguir.
   
    # Linhas iniciadas com o caractére(#) Sustenido,
    # hashtag, lasanha... são descartadas, por serem
    # consideradas, comentários.
    # linhas vazias ou em branco também serão 
    # descartdas nas próximas versões.
    
    \r\n|\n|g+
    \n\n+|\n|g+
    Brasil.*Dados Gerais||g+
    Chave de Acesso|ChaveAcesso: |g+
    (ChaveAcesso:) *([0-9 ]*)|$1 "$2"\n|g+
    (N.*mero)([0-9]*)(Vers.*XML.*)|Numero: "$2"\n$3|g+
    Vers.*XML([^\n]*)\n|VersaoXML: "$1"\n|g+
    NFe(.*)Modelo|Modelo#|g+
    ...
    Exibir (.*)Receita Federal\n(.*)|###FIM###\n|g+
    ([^\n]+)\n|\t$1,\n|g+
    (.*),\n\t###FIM###(.*)|ZZ#INICIO###$1\n###FIM###\n|g+ 
    ZZ#INICIO###\n\t,\n(.*)|ZZ#INICIO###$1\n###FIM###\n|g+
    ZZ#INICIO###(.*)###FIM###(.*)|NFe: {$1}|g+
    ^NFe: ...,\n(.*)$|NFe: {\n$1\n|g+


## Futuras melhorias

 1. Implementar a substituição de mais constantes simbólicas no texto a ser substituído além do **(\t** e do **\n)**, por valores literais.
 2. Ler dados da entrada-padrão caso não seja informado um arquivo de textos na entrada, fazendo, de fato, jus ao nome da aplicação de editor de Stream.
 3. Criar comandos de formatação da saída a moda **printf**
 4. Oferecer suporte a funções definidas pelo usuário, para serem disparadas  pré e pós a substituição dos texto.
 5. Permitir o fatiamento de um aquivo de entrada em vários arquivos de saída com base nas regras de validação das linhas.
 6. Acrescentar constantes simbólicas associados ao contexto de  execução como: @nomeArquivoEntrada, @DiretorioTrabalho, @Data, @Hora, ...
 7. Substituir o uso da método String.replaceAll, por Pattern e Matches (80% pronto);

# Como ajuda no Projeto

 - Me paguando umas cervejas Heineken!  
 - Apoiando projeto no GitHub!
 - Acessando e divulgando o meu site degestão de bolões e apostas na Mega Sena, o [Mega Power](https://megapower-loterias.com.br/)

![Site para gestão de jogos, bolões e apostas na Mega Sena](https://megapower-loterias.com.br/assets/img/illustrations/MegaPower-QRCode-Link.png)

