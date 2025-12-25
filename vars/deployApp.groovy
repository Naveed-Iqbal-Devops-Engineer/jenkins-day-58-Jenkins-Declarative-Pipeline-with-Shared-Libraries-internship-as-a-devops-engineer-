def call(String composeFile, String branch) {
    sh """
        docker-compose -f ${composeFile} --env-file .env down
        docker-compose -f ${composeFile} --env-file .env pull
        docker-compose -f ${composeFile} --env-file .env up -d --remove-orphans
    """
}
7. cleanupImages.groovy:
def call() {
    sh 'docker rmi ${BACKEND_TAG_DH} ${FRONTEND_TAG_DH} || true'
}
