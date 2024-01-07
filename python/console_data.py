import pymysql
import statistics
from scipy.stats import skew, kurtosis
import matplotlib.pyplot as plt


# Połączenie z bazą danych MySQL
connection = pymysql.connect(host="localhost", user="root", password="root", database="GMS", port=3306)
cursor = connection.cursor()

# Zapytanie SQL
query = """
    SELECT TIMESTAMPDIFF(YEAR, date_of_birth, CURDATE()) as Age, COUNT(*) as ClientsCount
    FROM client
    GROUP BY Age
    ORDER BY Age;
"""

# Wykonanie zapytania
cursor.execute(query)

# Pobranie wyników
results = cursor.fetchall()

# Zamknięcie połączenia
cursor.close()
connection.close()

# Przekształcenie wyników na słownik
data_dict = {age: clients_count for age, clients_count in results}


total_clients = 0
total_age = 0
ages = []

for age, clients_count in data_dict.items():
    total_clients += clients_count
    total_age += int(age) * clients_count
    ages.extend([int(age)] * clients_count)

if total_clients > 0:
    srednia_wiek = total_age / total_clients
    print(f"Średni wiek klienta wynosi: {srednia_wiek}")

    # Mediana
    median = statistics.median(ages)
    print(f"Mediana wieku klienta wynosi: {median}")

    # Moda
    moda = statistics.mode(ages)
    print(f"Moda wieku klienta wynosi: {moda}")

    # Wiek maksymalny i minimalny
    max_age = max(ages)
    min_age = min(ages)
    print(f"Wiek maksymalny klienta to: {max_age}")
    print(f"Wiek minimalny klienta to: {min_age}")

    # Wariancja
    wariancja = statistics.variance(ages)
    print(f"Wariancja wieku klienta wynosi: {wariancja}")

    # Odchylenie standardowe
    odchylenie_std = statistics.stdev(ages)
    print(f"Odchylenie standardowe wieku klienta wynosi: {odchylenie_std}")

    # Skośność
    skosnosc = skew(ages)
    print(f"Skośność wieku klienta wynosi: {skosnosc}")

    # Kurtoza
    kurtoza = kurtosis(ages)
    print(f"Kurtoza wieku klienta wynosi: {kurtoza}")
else:
    print("Brak danych klientów.")
