# authorization-server-oauth2

REDIS
- docker run --name redis -p 6379:6379 -d redis
- docker container start redis
- docker container ls
- docker container stop redis

GENERATING A KEY PAR WITH KEYTOOL
- On cmd: keytool -genkeypair -alias beverage -keyalg RSA -keypass 123456 -keystore beverage.jks -storepass 654321
  - RSA = Asymmetric encryption
  - Keystore = Where it will be store the key pair (you choose the name)
    - Storepass = password to open the keystore
  - keytool -list -keystore beverage.jks (to list the file)
  - Se quiser adicionar tempo de expiração maior (em num de dias): keytool ... -validity 9999
