def call(String composeFile, String branch) {
    sh """
        docker-compose -f ${composeFile} --env-file .env down
        docker-compose -f ${composeFile} --env-file .env pull
        docker-compose -f ${composeFile} --env-file .env up -d --remove-orphans
    """
}

