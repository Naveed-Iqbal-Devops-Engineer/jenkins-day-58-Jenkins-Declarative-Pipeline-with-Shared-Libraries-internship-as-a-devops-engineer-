def call() {
    sh 'docker push ${BACKEND_TAG_DH}'
    sh 'docker push ${FRONTEND_TAG_DH}'
}
