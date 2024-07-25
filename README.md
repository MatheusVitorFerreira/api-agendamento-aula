# Sistema de Agendamento de Aulas

## Visão Geral

Este projeto é um sistema de agendamento de aulas que permite que professores e alunos possam organizar suas atividades acadêmicas de forma eficiente. O sistema suporta múltiplas especializações para professores e horários distintos para cada especialização. Ele também permite o gerenciamento de disciplinas, aulas, inscrições e solicitações de remarcação.

## Diagrama UML

![diagrama](https://github.com/user-attachments/assets/3ac6c8d4-8d37-4c62-824e-29c7aaf08696)


## Funcionalidades

- *Gerenciamento de Usuários*: Cadastro e gerenciamento de informações pessoais de professores, alunos e administradores.
- *Gerenciamento de Disciplinas*: Criação e atribuição de disciplinas a professores.
- *Gerenciamento de Aulas*: Agendamento e organização de aulas com horários específicos e disciplinas.
- *Inscrição em Aulas*: Alunos podem se inscrever e cancelar inscrições em aulas.
- *Remarcação de Aulas*: Solicitações de remarcação de aulas com um fluxo de aprovação.
- *Horários de Disponibilidade*: Professores podem definir seus horários de disponibilidade para cada especialização.

## Estrutura do Projeto

O projeto é estruturado em várias classes principais:

1. *Person*: Classe base para todos os tipos de usuários (professores e alunos).
2. *Teacher*: Herda de Person, contém especializações e horários de disponibilidade.
3. *Student*: Herda de Person, mantém o registro das aulas nas quais o aluno está inscrito.
4. *Discipline*: Representa as disciplinas oferecidas.
5. *ScheduleClass*: Representa as aulas agendadas, incluindo data, horário, local e professor.
6. *Lesson*: Detalhes da aula específica, incluindo a capacidade de aplicar remarcações.
7. *Enrollment*: Registra a inscrição dos alunos nas aulas.
8. *TimeTable*: Define os horários de disponibilidade dos professores.
9. *RescheduleExpirationData*: Gerencia as solicitações de remarcação de aulas.
10. *StatusClass*: Enumeração para os diferentes estados de uma inscrição ou solicitação de remarcação.
