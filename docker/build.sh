#!/usr/bin/env bash

SCRIPTS_DIR=./scripts
KIT_MANAGER_DIR=../KitManager/Databases/cart_service

rm -rf ${SCRIPTS_DIR}
mkdir ${SCRIPTS_DIR}

cp -r ${KIT_MANAGER_DIR}/Tables ${SCRIPTS_DIR}/Tables/
cp -r ${KIT_MANAGER_DIR}/Views ${SCRIPTS_DIR}/Views/
cp -r ${KIT_MANAGER_DIR}/ForeignKeys ${SCRIPTS_DIR}/ForeignKeys/
cp -r ${KIT_MANAGER_DIR}/Triggers ${SCRIPTS_DIR}/Triggers/
cp -r ${KIT_MANAGER_DIR}/StoredProcedures ${SCRIPTS_DIR}/StoredProcedures/
cp -r ${KIT_MANAGER_DIR}/UserFunctions ${SCRIPTS_DIR}/UserFunctions/
cp -r ${KIT_MANAGER_DIR}/SeedData ${SCRIPTS_DIR}/SeedData/

#cp -r ${KIT_MANAGER_DIR}/DataScripts/** ${SCRIPTS_DIR}/DataScripts/

docker build -t ${1} .