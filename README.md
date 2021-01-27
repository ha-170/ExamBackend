[![Build Status](https://travis-ci.com/ha-170/ExamBackend.svg?branch=master)](https://travis-ci.com/ha-170/ExamBackend)

# Exam Backend 

## Brug af startkoden:

For at benytte sig af startkoden kræver det, at man har en local developer setup med en tilhørende droplet på DigitalOcean, som det blev beskrevet i Flow 1 Week 1 af 3. semester.

Det kræver en local user for sin MySQL database, som hedder "dev" med adgangskoden "ax2".

### I terminalen:
cd ind til mappen, hvor docker er installeret. 

kør "docker-compose build"

kør "docker-compose up -d"

I docker-compose.yml, ændr "CONNECTION_STR" til din egen connection string.

### I koden:

I EMF_CREATOR, ændr "CONNECTION_STR" til din egen connection string - linie 43 og 46.

I pom.xml-filen, ændr "remote.server" til din egen server-ip efterfulgt af "/manager/text" - linie 21.

I persistence.xml, ændr "property value" til din lokale database - linie 24.

I travis.yml, ændr "CREATE DATABASE ..." til navnet på din lokale test-database - linie 43. 

I security.SharedSecret, fjern/udkommenter "if(true){return "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA".getBytes();}" - linie 21-23.

SetupTestUsers.java er tilføjet i .gitignore, så der ikke pushes brugernavne og adgangskoder på GitHub. 
Klassen skal derfor oprettes manuelt, hvis dette ikke allerede er gjort. 

This document explains how to use this code (build, test and deploy), locally with maven, and remotely with maven controlled by Travis
 - [How to use](https://docs.google.com/document/d/1K6s6Tt65bzB8bCSE_NUE8alJrLRNTKCwax3GEm4OjOE/edit?usp=sharing)
