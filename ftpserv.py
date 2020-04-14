#! /usr/bin/Python
from pyftpdlib.authorizers import DummyAuthorizer
from pyftpdlib.handlers import FTPHandler
from pyftpdlib.servers import FTPServer

authorizer = DummyAuthorizer()
authorizer.add_user("1", "1", "/home/bartek/python/project/JPO", perm="elradfmw")
#authorizer.add_anonymous("/home/bartek/python/project/JPO/nobody")

handler = FTPHandler
handler.authorizer = authorizer

server = FTPServer(("10.22.114.17", 2121), handler)
server.max_cons = 50
server.serve_forever()
