green=\033[0;92m
yellow=\033[0;93m
red=\033[0;91m
blue=\033[0;94m
clear=\033[00m

DATABASE_NAME=testedb

PROJECT=GTrack Processing Node

## prints help message
help:
	@echo "\n${yellow}${PROJECT}\n----------------------\n${clear}"
	@awk '/^[a-zA-Z\-\_0-9\.%]+:/ { \
		helpMessage = match(lastLine, /^## (.*)/); \
		if (helpMessage) { \
			helpCommand = substr($$1, 0, index($$1, ":")); \
			helpMessage = substr(lastLine, RSTART + 3, RLENGTH); \
			printf "${blue}$$ make %s${clear} %s\n", helpCommand, helpMessage; \
		} \
	} \
	{ lastLine = $$0 }' $(MAKEFILE_LIST) | sort
	@printf "\n"

## create gateway environment
environment:
	docker-compose -f docker-start-gw.yml up
	
login-docker:
	docker login registry.gitlab.com

## database migration
migrate-db:
ifndef ENV
	$(eval ENV := local)
endif
	@echo "${blue}Iniciando a migração do banco de dados ${yellow}${DATABASE_NAME}${clear} com Ambiente ${yellow}${ENV}${clear}"
	@db-migrate up --config infra/db/database.json --env=${ENV}