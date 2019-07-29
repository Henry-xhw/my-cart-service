
CREATE DATABASE cart_service
GO

USE [cart_service]
GO

CREATE LOGIN [fnd_dev_usr] WITH PASSWORD = 'fnd_dev_usr$2008'
GO

CREATE USER [fnd_dev_usr] FOR LOGIN [fnd_dev_usr] WITH DEFAULT_SCHEMA = [dbo]
GO

ALTER ROLE [db_owner] ADD MEMBER [fnd_dev_usr]
GO

ALTER ROLE [db_datareader] ADD MEMBER [fnd_dev_usr]
GO

ALTER ROLE [db_datawriter] ADD MEMBER [fnd_dev_usr]
GO