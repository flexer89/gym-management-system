import matplotlib.pyplot as plt
import pymysql

# Połączenie z bazą danych MySQL
connection = pymysql.connect(host="localhost", user="root", password="root", database="GMS", port=3306)
cursor = connection.cursor()

# Zapytanie SQL
query = """
    SELECT TIMESTAMPDIFF(YEAR, date_of_birth, CURDATE()) as Age, COUNT(*) as Liczebnosc
    FROM client
    GROUP BY Age;
"""

# Wykonanie zapytania
cursor.execute(query)

# Pobranie wyników
results = cursor.fetchall()

# Rozdzielenie wyników na dwie listy: wiek i liczebność
ages = [result[0] for result in results]
counts = [result[1] for result in results]

# Obliczenia dodatkowe
total_clients = sum(counts)
frequency_relative = [count / total_clients for count in counts]
percentage = [freq * 100 for freq in frequency_relative]

# Zamknięcie połączenia
cursor.close()
connection.close()

# Tworzenie wykresu
plt.figure(figsize=(12, 8))

# Wykres słupkowy Liczebności z dokładnymi wartościami na słupek
plt.subplot(2, 1, 1)
bars = plt.bar(ages, counts, color='blue')
plt.xlabel('Wiek')
plt.ylabel('Liczebność')
plt.title('Rozkład wieku klientów siłowni')

# Dodawanie etykiet na każdy słupek
for bar in bars:
    yval = bar.get_height()
    plt.text(bar.get_x() + bar.get_width()/2, yval, round(yval, 2), ha='center', va='bottom', rotation='vertical')

# Wykres Częstości Względnej z dokładnymi wartościami na słupek
plt.subplot(2, 1, 2)
bars_relative = plt.bar(ages, frequency_relative, color='green')
plt.xlabel('Wiek')
plt.ylabel('Częstość względna')
plt.title('Częstość względna wieku klientów siłowni')

# Dodawanie etykiet na każdy słupek
for bar in bars_relative:
    yval_relative = bar.get_height()
    plt.text(bar.get_x() + bar.get_width()/2, yval_relative, f'{round(yval_relative*100, 2)}%', ha='center', va='bottom', rotation='vertical')

plt.tight_layout()
plt.show()

# Wykres Procentowy
plt.figure(figsize=(12, 8))
bars_percentage = plt.bar(ages, percentage, color='purple')
plt.xlabel('Wiek')
plt.ylabel('Procent')
plt.title('Procentowy udział wieku klientów siłowni')

# Dodawanie etykiet na każdy słupek
for bar in bars_percentage:
    yval_percentage = bar.get_height()
    plt.text(bar.get_x() + bar.get_width()/2, yval_percentage, f'{round(yval_percentage, 2)}%', ha='center', va='bottom', rotation='vertical')

plt.tight_layout()
plt.show()
