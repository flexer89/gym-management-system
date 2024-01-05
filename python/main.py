import mysql.connector
import random
import string
from datetime import datetime, timedelta
import hashlib

def generate_login(index):
    return f"client{index}"

def generate_name():
    first_names = ["Jan", "Anna", "Piotr", "Katarzyna", "Marcin", "Marta", "Michał", "Agnieszka", "Tomasz", "Karolina",
                   "Mateusz", "Natalia", "Grzegorz", "Joanna", "Kamil", "Magdalena", "Rafał", "Ewa", "Adam", "Patrycja"]
    return random.choice(first_names)

def generate_surname():
    last_names = ["Nowak", "Kowalski", "Wiśniewski", "Wójcik", "Kowalczyk", "Kamiński", "Lewandowski", "Zielińska", 
                  "Szymański", "Woźniak", "Jankowski", "Witkowski", "Pawlak", "Kaczmarek", "Włodarczyk", "Mazur", 
                  "Krajewska", "Nowicki", "Zając", "Olszewski"]
    return random.choice(last_names)

def generate_date_of_birth():
    start_date = datetime(1980, 1, 1)
    end_date = datetime(2005, 12, 31)
    random_date = start_date + timedelta(days=random.randint(0, (end_date - start_date).days))
    return random_date.strftime("%Y-%m-%d")

def generate_phone_number():
    return ''.join(random.choices(string.digits, k=9))

def generate_email(index):
    return f"client{index}@example.com"

def find_last_used_id():
    # Database credentials
    db_credentials = {
        "host": "localhost",
        "port": 3306,
        "user": "root",
        "password": "root",
        "database": "GMS"
    }

    try:
        # Connect to the database
        conn = mysql.connector.connect(**db_credentials)

        # Create a cursor object to execute SQL queries
        cursor = conn.cursor()

        # Execute the SQL query to find the last used ID
        query = "SELECT MAX(id) FROM client"
        cursor.execute(query)

        # Fetch the result
        last_used_id = cursor.fetchone()[0]

        # Close the cursor and connection
        cursor.close()
        conn.close()

        return last_used_id

    except mysql.connector.Error as err:
        print(f"Error: {err}")
        return None

def get_last_membership_card_id():
    try:
        # Connect to the MySQL database
        conn = mysql.connector.connect(
            host="localhost",
            port=3306,
            user="root",
            password="root",
            database="GMS"
        )
        cursor = conn.cursor()

        # Fetch the last used membership_card_id
        cursor.execute("SELECT MAX(id) FROM membership_card")
        last_membership_card_id = cursor.fetchone()[0]

        return last_membership_card_id

    except mysql.connector.Error as e:
        print(f"Error: {e}")
        return None

    finally:
        # Close the database connection
        if conn.is_connected():
            cursor.close()
            conn.close()

def generate_gym_data(entrance_date, exit_time):
    entrance_datetime = datetime.strptime(f'{entrance_date} {random.randint(6, 22):02d}:{random.randint(0, 59):02d}', '%Y-%m-%d %H:%M')

    entrance_duration = random.randint(30, 120)  # Random duration of stay between 30 to 120 minutes
    exit_datetime = entrance_datetime + timedelta(minutes=entrance_duration)

    gym_data = {
        'entrance_date': entrance_datetime.strftime('%Y-%m-%d'),
        'entrance_time': entrance_datetime.strftime('%H:%M'),
        'exit_date': exit_datetime.strftime('%Y-%m-%d'),
        'exit_time': exit_datetime.strftime('%H:%M'),
    }

    return gym_data

def insert_client_data(num_clients, last_used_id):
    # Connect to the MySQL database
    try:
        conn = mysql.connector.connect(
            host="localhost",
            port=3306,
            user="root",
            password="root",
            database="GMS"
        )
        cursor = conn.cursor()

        next_id_to_use = last_used_id + 1
        card_number = '00240407'  # Initial card number

        for i in range(1, num_clients + 1):
            login = f"10client{i}"
            name = generate_name()
            surname = generate_surname()
            dob = generate_date_of_birth()
            phone_number = generate_phone_number()
            email = generate_email(i)

            log_data = f"{next_id_to_use} {login} {name} {surname} {dob} {phone_number} {email}    {card_number}"
            print(log_data)

            password = 'ab92e806026cdf03da3301be0da72b0c624d482aea8123092fae2d29d4a39cbb'
            salt = '[B@737d46ef'

            cursor.execute("INSERT INTO client (first_name, last_name, date_of_birth, phone_number, email) VALUES (%s, %s, %s, %s, %s)",
                           (name, surname, dob, phone_number, email))

            client_id = cursor.lastrowid

            cursor.execute("INSERT INTO client_credentials (login, password, salt, client_id) VALUES (%s, %s, %s, %s)",
                           (login, password, salt, client_id))

            # Commit the changes to the database
            conn.commit()

            payment_query = "INSERT INTO payment (payment_date, amount, payment_method, client_id) VALUES ('2024-01-03', 477, 'card', %s)"
            cursor.execute(payment_query, (client_id,))

            membership_card_query = "INSERT INTO membership_card (card_number, expiration_date, type, all_gyms_access, original_gym_id, client_id) VALUES (%s, '2024-06-03', 'membership', 1, 1, %s)"
            cursor.execute(membership_card_query, (card_number, client_id))

            # Get the last generated membership_card_id
            last_membership_card_id = get_last_membership_card_id()+1

            # Update the client table with the last generated membership_card_id
            update_query = "UPDATE client SET membership_card_id = %s WHERE id = %s"
            cursor.execute(update_query, (last_membership_card_id, client_id))

            start_date = datetime.strptime('2024-01-03', '%Y-%m-%d')
            end_date = datetime.strptime('2024-05-01', '%Y-%m-%d')

            current_date = start_date
            while current_date <= end_date:
                entrance_date_str = current_date.strftime('%Y-%m-%d')
                exit_time = '19:19'  # You can change this if needed
                gym_id = 1

                if(random.randint(0, 1) == 0):
                    generated_data = generate_gym_data(entrance_date_str, exit_time)

                    # Display generated data for each date
                    print(generated_data)

                    entrance_date = generated_data['entrance_date']
                    entrance_time = generated_data['entrance_time']
                    exit_date = generated_data['exit_date']
                    exit_time = generated_data['exit_time']

                    # Insert Gym Visit record
                    insert_gym_visit_query = "INSERT INTO gym_visits (client_id, gym_id, entrance_date, entrance_time) VALUES (%s, %s, %s, %s)"
                    cursor.execute(insert_gym_visit_query, (next_id_to_use, gym_id, entrance_date, entrance_time))

                    # Update Gym Visit record
                    update_gym_visit_query = "UPDATE gym_visits SET exit_date = %s, exit_time = %s WHERE client_id = %s AND gym_id = %s AND exit_date IS NULL"
                    cursor.execute(update_gym_visit_query, (exit_date, exit_time, next_id_to_use, gym_id))

                # Move to the next date
                current_date += timedelta(days=1)
            # Gym Visits
            # gym_id = 1  # You may need to set the appropriate gym_id based on your data
            # entrance_date = '2024-01-03'
            # entrance_time = '18:05'
            # exit_date = '2024-01-03'
            # exit_time = '19:19'
            # membership_expiration_day='2024-06-03'


            # # Insert Gym Visit record
            # insert_gym_visit_query = "INSERT INTO gym_visits (client_id, gym_id, entrance_date, entrance_time) VALUES (%s, %s, %s, %s)"
            # cursor.execute(insert_gym_visit_query, (next_id_to_use, gym_id, entrance_date, entrance_time))

            # # Update Gym Visit record
            # update_gym_visit_query = "UPDATE gym_visits SET exit_date = %s, exit_time = %s WHERE client_id = %s AND gym_id = %s AND exit_date IS NULL"
            # cursor.execute(update_gym_visit_query, (exit_date, exit_time, next_id_to_use, gym_id))




            # Commit the changes to the database
            conn.commit()

            next_id_to_use += 1
            card_number_int = int(card_number)
            card_number_int += 1
            card_number = str(card_number_int).zfill(len(card_number))  # Ensure leading zeros are retained

    except mysql.connector.Error as e:
        print(f"Error: {e}")

    finally:
        # Close the database connection
        if conn.is_connected():
            cursor.close()
            conn.close()

def main():
    last_used_id = find_last_used_id()
    print(f"The last used ID in the 'client' table is: {last_used_id}")
    insert_client_data(5000,last_used_id)













if __name__ == "__main__":
    main()
