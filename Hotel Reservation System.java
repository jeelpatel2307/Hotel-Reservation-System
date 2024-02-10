hotel_reservation_system/
│
├── static/
│   ├── style.css
│   └── script.js
│
├── templates/
│   ├── index.html
│   └── reservation.html
│
├── app.py
└── reservations.db






from flask import Flask, render_template, request, redirect, url_for
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///reservations.db'
db = SQLAlchemy(app)

class Reservation(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(100), nullable=False)
    check_in_date = db.Column(db.String(10), nullable=False)
    check_out_date = db.Column(db.String(10), nullable=False)
    room_type = db.Column(db.String(50), nullable=False)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/reserve', methods=['POST'])
def reserve():
    name = request.form['name']
    check_in_date = request.form['check_in_date']
    check_out_date = request.form['check_out_date']
    room_type = request.form['room_type']
    new_reservation = Reservation(name=name, check_in_date=check_in_date, check_out_date=check_out_date, room_type=room_type)
    db.session.add(new_reservation)
    db.session.commit()
    return redirect(url_for('index'))

@app.route('/reservations')
def reservations():
    reservations = Reservation.query.all()
    return render_template('reservations.html', reservations=reservations)

if __name__ == '__main__':
    db.create_all()
    app.run(debug=True)






<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Reservation System</title>
    <link rel="stylesheet" href="{{ url_for('static', filename='style.css') }}">
</head>
<body>
    <h1>Hotel Reservation System</h1>
    <form action="/reserve" method="post">
        <input type="text" name="name" placeholder="Name" required>
        <input type="date" name="check_in_date" required>
        <input type="date" name="check_out_date" required>
        <select name="room_type" required>
            <option value="single">Single Room</option>
            <option value="double">Double Room</option>
            <option value="suite">Suite</option>
        </select>
        <button type="submit">Make Reservation</button>
    </form>
</body>
</html>






<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hotel Reservations</title>
    <link rel="stylesheet" href="{{ url_for('static', filename='style.css') }}">
</head>
<body>
    <h1>Hotel Reservations</h1>
    <ul>
        {% for reservation in reservations %}
            <li>{{ reservation.name }} - {{ reservation.check_in_date }} to {{ reservation.check_out_date }} - {{ reservation.room_type }}</li>
        {% endfor %}
    </ul>
</body>
</html>






body {
    font-family: Arial, sans-serif;
}

form {
    margin-bottom: 20px;
}

ul {
    list-style-type: none;
    padding: 0;
}

li {
    margin-bottom: 10px;
}
