
## Usage:
### help
```
Usage: scala throughFirewall.jar [OPTION]... [FILE]
Build a tunnel to connect to firewall behind .

Options:
 -h, --help show this help msg and exit.
 -public    use public server .
 -private   use private server .
 -client    use client socks server default.

Examples:
 scala throughFirewall.jar -public  -service 192.168.0.100:3008
 scala throughFirewall.jar -private -service 192.168.0.100:3008
 scala throughFirewall.jar -client  -listen  127.0.0.1:1080 -service 192.168.0.100:3008

Please put the ssl keystore under the program dir .
```

### help public

```
Usage: scala throughFirewall.jar -public [OPTION]... [FILE]
Start a public server to forward streams to firewall behind .

Options:
 -h, --help            show this help msg and exit .
 -service [host:port]  public server listen host and port, default is localhost:3008 .
 -ssl                  encrypt with ssl, default is ssl,param default without encrypt .
 -key FILE             keystore ,default is black.

 Examples:
  scala throughFirewall.jar -public  -service 192.168.0.100:3008
```


### help private:

```
Usage:scala throughFirewall.jar -private [OPTION]... [FILE]
Start a private server to forward streams from remote public server .

 -h, --help            show this help msg and exit .
 -service [host:port]  public server listen host and port, default is localhost:3008 .
 -ssl                  encrypt with ssl, default is ssl,param default without encrypt .
 -key FILE             keystore ,default is black.

 Examples:
  scala throughFirewall.jar -private -service 192.168.0.100:3008

```

### help client

```
Usage:scala throughFirewall.jar -private [OPTION]... [FILE]
Start a client to forward local streams to remote public server .

Options:
 -h,--help              show this help msg and exit .
 -listen [host:port]    local socks server listen host and port,default is localhost:1080 .
 -service [host:port]   public server listen host and port, default is localhost:3008 .
 -ssl                   encrypt with ssl, default is ssl,param default without encrypt .
 -key FILE              keystore ,default is black.

 Examples:
  scala throughFirewall.jar -client  -listen  127.0.0.1:1080 -service 192.168.0.100:3008

```

## License

```
Copyright 2016-2017 dogHere

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
