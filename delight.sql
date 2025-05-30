-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 05 Jul 2024 pada 17.25
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `dbicecream`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `delight`
--

CREATE TABLE `delight` (
  `id_transaksi` int(11) NOT NULL,
  `nama` varchar(100) DEFAULT NULL,
  `rasa` varchar(50) DEFAULT NULL,
  `harga` int(11) DEFAULT NULL,
  `jumlah_beli` int(11) DEFAULT NULL,
  `total_harga` int(11) DEFAULT NULL,
  `diskon` double DEFAULT NULL,
  `ppn` double DEFAULT NULL,
  `total_bayar` double DEFAULT NULL,
  `pembayaran` int(11) DEFAULT NULL,
  `kembalian` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `delight`
--

INSERT INTO `delight` (`id_transaksi`, `nama`, `rasa`, `harga`, `jumlah_beli`, `total_harga`, `diskon`, `ppn`, `total_bayar`, `pembayaran`, `kembalian`) VALUES
(111, 'Aryando', 'Coklat', 10000, 10, 100000, 15000, 2000, 87000, 90000, 3000),
(113, 'Putri', 'Vanilla', 15000, 2, 30000, 0, 600, 30600, 50000, 19400),
(114, 'Angel', 'Oreo', 18000, 3, 54000, 8100, 1080, 46980, 50000, 3020),
(121, 'Lia', 'Oreo', 18000, 5, 90000, 13500, 1800, 78300, 100000, 21700),
(123, 'Angelia', 'Coklat', 10000, 3, 30000, 4500, 600, 26100, 30000, 3900),
(132, 'Sky', 'Coklat', 10000, 7, 70000, 10500, 1400, 60900, 7000, -53900);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `delight`
--
ALTER TABLE `delight`
  ADD PRIMARY KEY (`id_transaksi`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
