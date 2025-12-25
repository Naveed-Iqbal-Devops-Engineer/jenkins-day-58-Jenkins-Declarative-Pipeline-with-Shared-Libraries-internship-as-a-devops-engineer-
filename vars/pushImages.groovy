def call() {
    sh 'docker push ${BACKEND_TAG_DH}'
    sh 'docker push ${FRONTEND_TAG_DH}'
}
5. prepareEnvFile.groovy:
def call() {
    writeFile file: '.env', text: """BACKEND_IMAGE=${BACKEND_TAG_DH}
FRONTEND_IMAGE=${FRONTEND_TAG_DH}
"""
}
