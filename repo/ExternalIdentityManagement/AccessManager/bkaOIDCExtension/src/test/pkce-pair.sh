code_verifier=$(openssl rand -base64 60 | tr -d "\n" | tr '/+' '_-' | tr -d '=')
echo "code_verifier=${code_verifier}"
code_challenge=$(printf ${code_verifier} | shasum -a 256 | head -c 64 | xxd -r -p - | openssl base64 | tr '/+' '_-' | tr -d '=')
echo "code_challenge=${code_challenge}"
##echo "copied the code_challenge to your clipboard"
