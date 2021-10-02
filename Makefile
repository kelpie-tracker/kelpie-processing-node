environment:
	docker-compose -f docker-start-gw.yml up
	
login-docker:
	docker login registry.gitlab.com