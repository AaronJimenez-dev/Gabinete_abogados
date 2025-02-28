-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 28-02-2025 a las 19:42:47
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gabinete_abogados`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `abogado`
--

CREATE TABLE `abogado` (
  `dni` varchar(9) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `abogado`
--

INSERT INTO `abogado` (`dni`) VALUES
('09090909M');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `caso`
--

CREATE TABLE `caso` (
  `num_expediente` int(4) NOT NULL,
  `cliente_dni` varchar(9) DEFAULT NULL,
  `juicio_id` int(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `caso_abogado`
--

CREATE TABLE `caso_abogado` (
  `num_expediente` int(4) NOT NULL,
  `abogado_dni` varchar(9) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `dni` varchar(9) NOT NULL,
  `num_tlfn` int(9) NOT NULL,
  `correo` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`dni`, `num_tlfn`, `correo`) VALUES
('25236963M', 123456789, 'carlos.m@gmail.com');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `juicio`
--

CREATE TABLE `juicio` (
  `fecha_inicio` date DEFAULT NULL,
  `fecha_fin` date DEFAULT NULL,
  `estado` varchar(10) DEFAULT NULL,
  `id` int(4) NOT NULL
) ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `persona`
--

CREATE TABLE `persona` (
  `dni` varchar(9) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `apellido` varchar(50) NOT NULL,
  `direccion` varchar(80) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `persona`
--

INSERT INTO `persona` (`dni`, `nombre`, `apellido`, `direccion`) VALUES
('09090909M', 'Manolito', 'Pene', 'Jamon Jamon 4, 3A'),
('25236963M', 'Carlos', 'Perez', 'Calle Panama 10, 3A');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `abogado`
--
ALTER TABLE `abogado`
  ADD PRIMARY KEY (`dni`);

--
-- Indices de la tabla `caso`
--
ALTER TABLE `caso`
  ADD PRIMARY KEY (`num_expediente`),
  ADD KEY `cas_cli_fk` (`cliente_dni`),
  ADD KEY `cas_jui_fk` (`juicio_id`);

--
-- Indices de la tabla `caso_abogado`
--
ALTER TABLE `caso_abogado`
  ADD PRIMARY KEY (`num_expediente`,`abogado_dni`),
  ADD KEY `cab_abo_fk` (`abogado_dni`);

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`dni`);

--
-- Indices de la tabla `juicio`
--
ALTER TABLE `juicio`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `persona`
--
ALTER TABLE `persona`
  ADD PRIMARY KEY (`dni`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `caso`
--
ALTER TABLE `caso`
  MODIFY `num_expediente` int(4) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `juicio`
--
ALTER TABLE `juicio`
  MODIFY `id` int(4) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `abogado`
--
ALTER TABLE `abogado`
  ADD CONSTRAINT `gab_abo_fk` FOREIGN KEY (`dni`) REFERENCES `persona` (`dni`);

--
-- Filtros para la tabla `caso`
--
ALTER TABLE `caso`
  ADD CONSTRAINT `cas_cli_fk` FOREIGN KEY (`cliente_dni`) REFERENCES `cliente` (`dni`),
  ADD CONSTRAINT `cas_jui_fk` FOREIGN KEY (`juicio_id`) REFERENCES `juicio` (`id`);

--
-- Filtros para la tabla `caso_abogado`
--
ALTER TABLE `caso_abogado`
  ADD CONSTRAINT `cab_abo_fk` FOREIGN KEY (`abogado_dni`) REFERENCES `abogado` (`dni`),
  ADD CONSTRAINT `cab_nex_fk` FOREIGN KEY (`num_expediente`) REFERENCES `caso` (`num_expediente`);

--
-- Filtros para la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD CONSTRAINT `gab_cli_fk` FOREIGN KEY (`dni`) REFERENCES `persona` (`dni`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
