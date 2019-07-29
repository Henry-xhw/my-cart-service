[![coverage](http://sonarqube-web-01w.dev.activenetwork.com:8080/api/badges/measure?key=ActiveWorks:cart-service&metric=coverage)](http://sonarqube-web-01w.dev.activenetwork.com:8080/component_measures/metric/coverage/list?id=ActiveWorks:cart-service)
[![sqale_debt_ratio](http://sonarqube-web-01w.dev.activenetwork.com:8080/api/badges/measure?key=ActiveWorks:cart-service&metric=sqale_debt_ratio)](http://sonarqube-web-01w.dev.activenetwork.com:8080/component_measures/metric/sqale_debt_ratio/list?id=ActiveWorks:cart-service)
[![reliability_rating](http://sonarqube-web-01w.dev.activenetwork.com:8080/api/badges/measure?key=ActiveWorks:cart-service&metric=reliability_rating)](http://sonarqube-web-01w.dev.activenetwork.com:8080/component_measures?id=ActiveWorks%3Acart-service&metric=Reliability)
[![new_reliability_rating](http://sonarqube-web-01w.dev.activenetwork.com:8080/api/badges/measure?key=ActiveWorks:cart-service&metric=new_reliability_rating)](http://sonarqube-web-01w.dev.activenetwork.com:8080/component_measures?id=ActiveWorks%3Acart-service&metric=new_reliability_rating)
[![code_smells](http://sonarqube-web-01w.dev.activenetwork.com:8080/api/badges/measure?key=ActiveWorks:cart-service&metric=code_smells)](http://sonarqube-web-01w.dev.activenetwork.com:8080/component_measures?id=ActiveWorks%3Acart-service&metric=code_smells)
[![Lines of Code](http://sonarqube-web-01w.dev.activenetwork.com:8080/api/badges/measure?key=ActiveWorks:cart-service&metric=ncloc)](http://sonarqube-web-01w.dev.activenetwork.com:8080/component_measures?id=ActiveWorks%3Acart-service&metric=ncloc)
[![violations](http://sonarqube-web-01w.dev.activenetwork.com:8080/api/badges/measure?key=ActiveWorks:cart-service&metric=violations)](http://sonarqube-web-01w.dev.activenetwork.com:8080/component_measures?id=ActiveWorks%3Acart-service&metric=violations)

# Cart service

## Build from source
```sh
git clone git@gitlab.dev.activenetwork.com:PlatformServices/cart-service.git
cd cart-service
```

Build
```sh
./gradlew clean build
```

Run
```sh
./gradlew bootRun
```

## API doc

See [API doc](http://share-apidocs.activenetwork.com/fbi/cart-service/)

## Errors and bugs
If something is not behaving intuitively, it is a bug and should be reported. For business/function related bugs, report it in [CORP JIRA](https://jirafnd.dev.activenetwork.com/browse/FNDFIN) by creating a bug, for non-functional issue or code/design improvements, report it [here](https://gitlab.dev.activenetwork.com/PlatformServices/cart-service/issues) by creating an issue.

Help us fix the problem as quickly as possible by following [Mozilla's guidelines for reporting bugs.](https://developer.mozilla.org/en-US/docs/Mozilla/QA/Bug_writing_guidelines#General_Outline_of_a_Bug_Report)

## Patches and merge requests

Patches are welcome. Here's the suggested workflow:

* Clone the project
* Create a branch, branch should be named to indicated the change or start with a JIRA ticket number
* Make feature addition or bug fix
* Send a merge request with a description of the actual work