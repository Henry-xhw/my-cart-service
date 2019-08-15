#!/usr/bin/env sh

CURRENT_VERSION=$(sed -n 2p ./version.properties | sed -E 's/[a-zA-Z]*=([0-9.]*).*/\1/')
LOCAL_DOC_PATH='build/asciidoc/html5'
REMOTE_SERVER='sharedoc@share-apidocs.activenetwork.com'
REMOTE_DOC_PATH='/opt/active/docs/oms/cart-service'

echo 'Making dir if not exist'
echo "Current version is ${CURRENT_VERSION}"
ssh ${REMOTE_SERVER} "[ -d ${REMOTE_DOC_PATH}/${CURRENT_VERSION} ] && echo ok || mkdir -p ${REMOTE_DOC_PATH}/${CURRENT_VERSION}"
echo 'Making dir done'

echo 'Publishing API doc'
uploadDocCmd="scp -r ${LOCAL_DOC_PATH}/* ${REMOTE_SERVER}:${REMOTE_DOC_PATH}/${CURRENT_VERSION}"
#upload the files to doc server:path
#e.g. rsync -avz -e ssh build/asciidoc/html5/* sharedoc@share-apidocs.activenetwork.com:/opt/active/docs/oms/cart-service/0.0.1-SNAPSHOT
${uploadDocCmd}

#change the remote latest.html to load the index.html in the latest version folder
echo "<script>window.location ='./${CURRENT_VERSION}/index.html'</script>" | ssh ${REMOTE_SERVER} -T "cat > ${REMOTE_DOC_PATH}/latest.html"
echo 'Publishing API doc done'