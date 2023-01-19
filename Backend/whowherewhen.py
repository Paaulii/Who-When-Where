#!/usr/bin/python3
from flask import Flask, jsonify, request, send_file, make_response
import json
import sys
import base64
from flask_mysqldb import MySQL
import uuid
import hashlib

app = Flask(__name__)

app.config['MYSQL_HOST'] = 'localhost'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = 'password'
app.config['MYSQL_DB'] = 'ktogdziekiedy'

mysql = MySQL(app)

def logging(e):
    try:
        with open("/var/log/whowherewhen.log", "a") as f:
            print(str(e), file=f)
    except Exception as ex:
        print(ex)

def checkpw(providedText, hashedText):
    _hashedText, salt = hashedText.split(':')
    return _hashedText == hashlib.sha256(salt.encode() + providedText.encode()).hexdigest()

def loginHeaderDb(auth_header):
    auth_header = base64.b64decode(auth_header).decode("utf-8")
    username, password = auth_header.split(":", 1)
    
    try:
        cursor = mysql.connection.cursor()
        query = ("SELECT password FROM user WHERE login = %s")
        cursor.execute(query, (username,))
        data = cursor.fetchone()
        cursor.close()
    except Exception as e:
        logging(e)

    if not bool(data):
        return False
    else:
        hashed_password = data[0]
        return checkpw(password, hashed_password)

def getUsersDb():
    try:
        cursor = mysql.connection.cursor()
        query = ("SELECT * FROM user")
        cursor.execute(query)
        data = cursor.fetchall() 
        cursor.close()
    except Exception as e:
        logging(e)
    
    users = []
    for user in data:
        users.append({"id_u": user[0], "name": user[1], "surname": user[2], "login": user[3]})
    return(json.dumps(users))

def getUserDb(username):
    try:
        cursor = mysql.connection.cursor()
        query = ("SELECT * FROM user WHERE login = %s")
        cursor.execute(query, (username,))
        data = cursor.fetchall()
        cursor.close()
    except Exception as e:
        logging(e)

    user = []
    if data is not None:
        user.append({"id_u": data[0], "name": data[1], "surname": data[2], "login": data[3]})
    return(json.dumps(user))

def getTasksDb(drop_id, status, category, one_id, id_u):
    drop = False
    one = False
    given_id = "%"

    try:
        drop_id = int(drop_id)
        one_id = int(one_id)
    except:
        drop_id = 0
        one_id = 0
    
    if drop_id == 0 and one_id != 0:
        one = True
        given_id = one_id
    elif drop_id !=0 and one_id == 0:
        drop = True
        given_id = drop_id 
    
    try:
        cursor = mysql.connection.cursor()
        query = ("SELECT * FROM task WHERE status {} %s AND id_t {} %s AND category {} %s AND id_u {} %s".format("LIKE" if status == "%" else "=", "=" if one else "!=" if drop else "LIKE","LIKE" if category == "%" else "=", "LIKE" if id_u == "%" else "="))
        cursor.execute(query, (status, given_id, category, id_u))
        data = cursor.fetchall()
        cursor.close()
    except Exception as e:
        logging(e)

    if len(data) == 0:
        return json.dumps(list());
    
    tasks = []
    for task in data:
        tasks.append({"id_t": task[0], "title": task[1], "description": task[2], "category": task[3], "status": task[4], "estimatedTime": task[5], "realTime": task[6], "id_u": task[7], "blockedBy": task[8]})
    return(json.dumps(tasks if len(tasks) > 1 and one_id >= 0 else tasks[0]))

def addTaskDb(title, description, category, status, estimatedTime, realTime, id_u, blockedBy):
    try:
        query = ("INSERT INTO task (title, description, category, status, estimatedTime, realTime, id_u, blockedBy) VALUES (%s,%s,%s,%s,%s,%s,%s,%s)")
        cursor = mysql.connection.cursor()
        cursor.execute(query, (title, description, category, status, estimatedTime, realTime, id_u, blockedBy))
        mysql.connection.commit()
        cursor.close()
    except Exception as e:
        logging(e)

def updateTaskDb(id_t, title, description, category, status, estimatedTime, realTime, id_u, blockedBy):
    try:
        query = ("UPDATE task SET title=%s, description=%s, category=%s, status=%s, estimatedTime=%s, realTime=%s, id_u=%s, blockedBy=%s WHERE id_t=%s")
        cursor = mysql.connection.cursor()
        cursor.execute(query, (title, description, category, status, estimatedTime, realTime, id_u, blockedBy,id_t))
        mysql.connection.commit()
        cursor.close()
    except Exception as e:
        logging(e)

def deleteTaskDb(id_t):
    try:
        query = ("DELETE FROM task WHERE id_t=%s")
        cursor = mysql.connection.cursor()
        cursor.execute(query, (id_t,))
        mysql.connection.commit()
        cursor.close()
    except Exception as e:
        logging(e)

@app.before_request
def login_request():
    if "login" not in request.url_rule.rule:
        try:
            login_result = loginHeaderDb(request.headers["Authorization"].encode("utf-8"))
        except Exception as e:
            logging(e)
            return make_response("", 404)

        if not login_result:
            return make_response("", 404)

@app.route('/login', methods=['POST']) 
def login():
    try:
        login_result = loginHeaderDb(request.headers["Authorization"].encode("utf-8"))
    except Exception as e:
        logging(e)
        return make_response("", 404)

    if login_result:
        return make_response("True", 200)
    else:
        return make_response("False", 200)

@app.route('/users', methods=['GET'])
def getUsers():
    return make_response(getUsersDb(), 200)

@app.route('/user', methods=['GET'])
def getUser():
    return make_response(getUserDb(request.args.get('username')), 200)

@app.route('/tasks', methods=['GET'])
def getTasks():
    drop_id = "0" if "drop" not in request.args else request.args.get("drop")
    one_id = "0" if "id" not in request.args else request.args.get("id") 
    status = "%" if "status" not in request.args else request.args.get("status")
    category = "%" if "category" not in request.args else request.args.get("category")
    id_u = "%" if "id_u" not in request.args else request.args.get("id_u")
    return make_response(getTasksDb(drop_id, status, category, one_id, id_u), 200)

@app.route('/addtask', methods=['POST'])
def addTask():
    data = request.json
    addTaskDb(data["title"], data["description"], data["category"], data["status"], data["estimatedTime"], data["realTime"], data["id_u"], data["blockedBy"])
    return make_response("OK", 200)

@app.route('/updatetask', methods=['POST'])
def updateTask():
    data = request.json
    updateTaskDb(data["id_t"], data["title"], data["description"], data["category"], data["status"], data["estimatedTime"], data["realTime"], data["id_u"], data["blockedBy"])
    return make_response("OK", 200)

@app.route('/deletetask', methods=['POST'])
def deleteTask():
    data = request.json
    deleteTaskDb(data["id_t"])
    return make_response("OK", 200)

if __name__ == '__main__':
    app.run(debug=False, use_reloader=False, host="127.0.0.1", port=30000)
