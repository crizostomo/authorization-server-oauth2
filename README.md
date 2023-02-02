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

EXPORTING A PUBLIC KEY INSIDE THE CERTIFICATE WITH KEYTOOL
- On cmd: keytool -export -rfc -alias beverage -keystore beverage.jks -file beverage-cert.pem
  - rfc = Textual format
  - pem = privacy enhanced mail
- Option 1: On cmd (if openssl is installed): openssl x509 -pubkey -noout -in beverage-cert.pem > beverage-pkey.pem
- Option 2: GET in the endpoint http://localhost:8081/oauth/token_key
