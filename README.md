# Backend do TCC (FATEC - Spring MVC c/ JSON)

## Introdução
Este projeto foi desenvolvido como parte da disciplina de Programação Orientada a Objetos (POO). Este projeto tem como objetivo aplicar os conceitos de uma aplicação Java utilizando o framework Spring MVC e o formato de dados JSON.

## Alunos
* João Tadeu da Silva Marangoni
* Lucas Rodrigues de Moraes
* Cauê Trani de Mira
* Wenzel Francisco Moreira da Costa

## Descrição do projeto
A aplicação desenvolvida simula o sistema de gerenciamento de uma empresa de decoração de festas (Maira Gasparini Decoração de Festas). O sistema permite a administração de diversos aspectos do negócio, incluindo o cadastro de clientes, gerenciamento de eventos, agendamento de eventos e seleção de tipo de decorações. Utilizando a arquitetura Spring MVC, o projeto segue os princípios de desenvolvimento de software modular e escalável, facilitando a manutenção e a expansão da aplicação.

## Funcionalidades
- **Cadastro de clientes:** Permite o registro de novos clientes, edição e exclusão de informações.
- **Gerenciamento de Eventos:** Permite a criação, edição e exclusão de eventos, além de associar clientes aos eventos.
- **Seleção de Decorações:** Oferece uma interface para selecionar o tipo de decoração e personalizar itens para cada evento.
- **Acompanhamento de Pedidos:** Permite visualizar o status dos pedidos e acompanhar a entrega e execução dos serviços.

## Tecnologias Utilizadas
- **Java:** Linguagem de programação utilizada para desenvolver a aplicação.
-	**Spring MVC:** Framework utilizado para implementar o padrão Model-View-Controller (MVC).
- **JSON:** Formato de intercâmbio de dados utilizado para comunicação entre o front-end e o back-end.
- **API:** Um conjunto de definições e protocolos que permite que diferentes sistemas de software se comuniquem entre si.
- **Banco de Dados:** Utilizado para armazenar as informações dos clientes, eventos, decorações e pedidos.

## Estrutura MVC do Projeto
O projeto está organizado nas seguintes pastas e arquivos principais:
- **src/main/java:** Contém o código fonte Java.
- **controllers:** Controladores responsáveis por lidar com as requisições HTTP.
- **models:** Classes que representam as entidades do sistema.
- **repositories:** Interfaces que gerenciam a persistência dos dados.
- **services:** Classes de serviço que contêm a lógica de negócio.
- **src/main/resources:** Contém arquivos de configuração e templates.
- **src/test/java:** Contém os testes unitários e de integração.
>spring.datasource.url=URL DA DATABASE

>spring.datasource.username=USERNAME DA DATABASE

>spring.datasource.password=SENHA DA DATABASE

## Como executar
Para executar a aplicação localmente, siga os passos abaixo:
- Clone o repositório do programa usando: git clone https://github.com/devMRNGN/EventManagerBackend.git
- Abra o projeto no seu **VSCODE**
- Configure o banco de dados conforme o necessário no arquivo application.properties
- Execute a classe principal para iniciar a aplicação.
- **OBS:** Para que o projeto funcione é necessário possuir as seguintes extensões **(Extension Pack for Java, Spring Boot Extension Pack)**

## Contato
Para mais informações, entre em contato com João Marangoni através do e-mail devmarangoni@gmail.com.
