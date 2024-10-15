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
   
         docker network create docker-network
     
3. **Fazer o Build da Aplicação:**
   - Execute o comando para construir a imagem da aplicação:
     
         docker build --tag agenda-aulas-api .
 
4. **Subir a Aplicação com Docker Compose:**
   Agora, você irá utilizar o docker-compose-qa.yml para subir os containers da aplicação, banco de dados e o pgAdmin.

         docker-compose -f docker-compose-qa.yml up --build

- Constrói os containers especificados no arquivo docker-compose-qa.yml.
- Utiliza a imagem agenda-aulas-api que você criou.
- Inicia os serviços definidos (banco de dados Postgres, aplicação api-agenda, e pgAdmin).


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



