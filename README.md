# Sistema de Agendamento de Aulas

## Visão Geral

O projeto é um sistema de Gerenciamento Acadêmico desenvolvido para otimizar a administração e a organização de instituições de ensino. Ele oferece uma plataforma integrada que facilita o gerenciamento de informações de usuários, disciplinas e aulas, bem como a interação entre professores e alunos.

## Diagrama UML

![diagrama](https://github.com/MatheusVitorFerreira/ChatLive/blob/main/Documente%20seus%20sistemas%20(6).png)
## Pré-requisitos

### Docker

Para executar este projeto, você precisará do Docker instalado em sua máquina. Siga os passos abaixo para configurar o ambiente:

1. **Instalação do Docker Desktop:**
   - Baixe e instale o [Docker Desktop](https://www.docker.com/get-started).

2. **Criar uma rede Docker:**
   - Após a instalação, crie uma rede Docker para o projeto com o seguinte comando:
   
     docker network create <nome_da_rede>
  
3. **Subir o banco de dados em Docker:**
   - Utilize o comando abaixo para subir o banco de dados PostgreSQL:
     
     docker run --name <nome_do_container> --network <nome_da_rede> -e POSTGRES_USER=<USER> -e POSTGRES_PASSWORD=<PASSWORD> -e POSTGRES_DB=schedule -p 5432:5432 -d postgres
    
4. **Fazer o Build da Aplicação:**
   - Execute o comando para construir a imagem da aplicação:
   - 
     docker build --tag <nome_da_imagem> .
 
5. **Criar o Container para subir a aplicação:**
   - Inicie o container da aplicação com o comando:

     docker run --name agenda-aulas-api -p 8080:8080 --network <nome_da_rede> <nome_da_imagem>

**Observação:** Substitua `<nome_da_rede>`, `<nome_do_container>`, `<USER>`, `<PASSWORD>`, e `<nome_da_imagem>` pelos valores correspondentes à sua configuração.

 
## Funcionalidades

- *Gerenciamento de Usuários:* Permite o cadastro e gerenciamento de informações pessoais para professores, alunos e administradores. Inclui funcionalidades para atualizar dados, gerenciar permissões e visualizar perfis.

- *Gerenciamento de Disciplinas:* Facilita a criação e atribuição de disciplinas aos professores. Oferece uma interface para definir e modificar as matérias oferecidas, assim como vincular cada disciplina ao respectivo educador.

- *Gerenciamento de Aulas:* Organiza e agenda aulas com horários específicos e disciplinas associadas. Inclui ferramentas para planejar o calendário acadêmico e garantir que as aulas estejam bem distribuídas e alinhadas com o horário disponível dos professores.

- *Inscrição em Aulas:* Permite que os alunos se inscrevam e cancelem inscrições em aulas conforme a disponibilidade. Inclui mecanismos para visualizar aulas disponíveis, se registrar e gerenciar sua participação.

- *Horários de Disponibilidade:* Professores podem definir e atualizar seus horários de disponibilidade para cada especialização. Esta funcionalidade garante que a agenda de aulas esteja sincronizada com os períodos em que os professores estão disponíveis.

- *Verificação de Conflitos de Aulas:* Identifica e resolve conflitos de agendamento entre aulas, garantindo que não haja sobreposição de horários para professores e alunos. O sistema verifica automaticamente possíveis conflitos e sugere ajustes quando necessário.

- *Verificação de Vagas Disponíveis:* Monitora e exibe a disponibilidade de vagas em disciplinas e aulas, permitindo que os alunos saibam quais opções estão abertas para inscrição.

# Documentação e Interface de API
 Para facilitar o uso e a integração com outros sistemas, todo o sistema é documentado utilizando Swagger. A documentação interativa do Swagger oferece uma visão detalhada das APIs disponíveis, permitindo fácil consulta e compreensão das funcionalidades oferecidas pelo sistema.
 
Endpoint Swagger: /swagger-ui/index.html#/



