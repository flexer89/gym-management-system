-- czestosc wiekowa klientow 
SELECT TIMESTAMPDIFF(YEAR, date_of_birth, CURDATE()) as Age, COUNT(*) as Liczebnosc
FROM client
GROUP BY Age;

