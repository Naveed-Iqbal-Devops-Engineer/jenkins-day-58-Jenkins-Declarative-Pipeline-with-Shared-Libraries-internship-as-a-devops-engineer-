def call() {
    sh 'docker rmi ${BACKEND_TAG_DH} ${FRONTEND_TAG_DH} || true'
}
