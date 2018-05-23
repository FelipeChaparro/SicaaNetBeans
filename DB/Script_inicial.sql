
CREATE TABLE `AreasActuacion` (
  `ID` int(3) NOT NULL,
  `Nombre` varchar(256) DEFAULT NULL,
  `IdPersona` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `Departamento` (
  `ID` int(3) NOT NULL,
  `IdFacultad` int(3) NOT NULL,
  `Nombre` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `Departamento` (`ID`, `IdFacultad`, `Nombre`) VALUES
(1, 1, 'Ingeniería de Sistemas');


CREATE TABLE `Evento` (
  `ID` int(3) NOT NULL,
  `Nombre` varchar(32) NOT NULL,
  `Tipo` varchar(32) NOT NULL,
  `IdPublicacion` int(3) NOT NULL,
  `Ambito` varchar(20) NOT NULL,
  `FechaInicio` date NOT NULL,
  `FechaFin` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `Evento_Persona` (
  `IdPersona` int(3) NOT NULL,
  `IdEvento` int(3) NOT NULL,
  `Rol` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `Facultad` (
  `ID` int(3) NOT NULL,
  `Nombre` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `Facultad` (`ID`, `Nombre`) VALUES
(1, 'Ingeniería');

CREATE TABLE `FormacionAcademica` (
  `ID` int(3) NOT NULL,
  `IdPersona` int(3) NOT NULL,
  `Categoria` varchar(100) NOT NULL,
  `Titulo` varchar(50) NOT NULL,
  `Universidad` varchar(100) NOT NULL,
  `Descripcion` varchar(200) NOT NULL,
  `FechaInicio` date NOT NULL,
  `FechaFin` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `Idioma_Persona` (
  `IdPersona` int(3) NOT NULL,
  `IdIdioma` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `Idiomas` (
  `ID` int(3) NOT NULL,
  `Nombre` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `Idiomas` (`ID`, `Nombre`) VALUES
(1, 'Francés'),
(2, 'Inglés'),
(3, 'Español'),
(4, 'Ingls'),
(5, 'Portugus'),
(6, 'Francs'),
(7, 'Portugués');


CREATE TABLE `Medallas` (
  `IdPersona` int(3) NOT NULL,
  `Bilingue` tinyint(1) NOT NULL DEFAULT '0',
  `Cientifico` tinyint(1) NOT NULL DEFAULT '0',
  `Director` tinyint(1) NOT NULL DEFAULT '0',
  `Doctor` tinyint(1) NOT NULL DEFAULT '0',
  `Investigador` tinyint(1) NOT NULL DEFAULT '0',
  `Jefe` tinyint(1) NOT NULL DEFAULT '0',
  `Administrativo` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `Persona` (
  `ID` int(6) NOT NULL,
  `Nombre` varchar(100) NOT NULL,
  `Registrado` tinyint(1) NOT NULL,
  `EstadoSistema` tinyint(1) NOT NULL,
  `URLCvLAC` varchar(200) DEFAULT NULL,
  `URLGoogleScholar` varchar(200) DEFAULT NULL,
  `URLResearchGate` varchar(200) DEFAULT NULL,
  `URLImagen` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `Persona_Publicacion` (
  `IdPersona` int(3) NOT NULL,
  `IdPublicacion` int(3) NOT NULL,
  `Rol` varchar(50) DEFAULT NULL,
  `EstadoPublicacion` varchar(32) NOT NULL,
  `EstadoSistema` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `Profesor` (
  `IdPersona` int(3) NOT NULL,
  `IdDepartamento` int(3) NOT NULL,
  `Nacionalidad` varchar(32) DEFAULT NULL,
  `Categoria` varchar(150) DEFAULT NULL,
  `Sexo` varchar(32) DEFAULT NULL,
  `FechaInicio` date DEFAULT NULL,
  `FechaFin` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `Publicacion` (
  `ID` int(6) NOT NULL,
  `Titulo` text NOT NULL,
  `Tipo` varchar(100) DEFAULT NULL,
  `codigoPublicacion` varchar(32) DEFAULT NULL,
  `Lugar` varchar(500) DEFAULT NULL,
  `Editorial` varchar(300) DEFAULT NULL,
  `FechaInicio` date DEFAULT NULL,
  `Extraido` varchar(100) NOT NULL,
  `tipoEspecifico` varchar(200) DEFAULT NULL,
  `duracion` varchar(50) DEFAULT NULL,
  `plataforma` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `PublicacionDudosa` (
  `ID` int(3) NOT NULL,
  `Titulo` varchar(400) NOT NULL,
  `Tipo` varchar(25) DEFAULT NULL,
  `codigoPublicacion` varchar(32) DEFAULT NULL,
  `Lugar` varchar(200) DEFAULT NULL,
  `Editorial` varchar(300) DEFAULT NULL,
  `FechaInicio` date DEFAULT NULL,
  `Extraido` varchar(100) NOT NULL,
  `EstadoSistema` tinyint(1) NOT NULL,
  `tipoEspecifico` varchar(200) DEFAULT NULL,
  `duracion` text,
  `plataforma` varchar(200) DEFAULT NULL,
  `idPublicacionDudosa` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE `Publicacion_Evento` (
  `IdPublicacion` int(3) NOT NULL,
  `IdEvento` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `PuntosPublicacion` (
  `ID` int(3) NOT NULL,
  `IdPersona` int(3) NOT NULL,
  `Puntos` int(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `datos_usuarios` (
  `id` int(11) NOT NULL,
  `user_correo` varchar(255) NOT NULL DEFAULT '',
  `user_password` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `parametros_sistema` (
  `tiempo_sesion` int(11) NOT NULL DEFAULT '5',
  `numero_podio_publicaciones` int(11) NOT NULL DEFAULT '3',
  `numero_publicaciones_recientes` int(11) NOT NULL DEFAULT '5',
  `numero_nube_palabras` int(11) NOT NULL DEFAULT '100',
  `cookieResearch` text,
  `cookieGoogle` text
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `parametros_sistema` (`tiempo_sesion`, `numero_podio_publicaciones`, `numero_publicaciones_recientes`, `numero_nube_palabras`, `cookieResearch`, `cookieGoogle`) VALUES
(5, 3, 5, 100, 'sid=un1rndUjmNUmYuiUFW169rl26LXOkLYEvamN20giMjRRbfR7KvSZmDFfSkqXatGO08OEPbT9ys7g4B8KalMBsh3gSahc97ZZnWnvbkhH4qGCZJwVSedW7xJvHo5dZcdK; did=IY1JjQc2QZEZ1LgHPOIj1AvC84GjK0t71xeOtRIqz622I3TJY5rKusa4zOi8jf03; ptc=RG1.1807906720633944214.1526851879; chseen=1; captui=MzVkNWZlY2UzY2YxZDI3MWU5N2MyMzc1NjBlMjk1YTY1M2ZlNmI1MGEwN2YxZmQ0MWU0ZDg4MjIyZTc5ZDI4MV9ZUmZKM2t5RTFFMklZMno5aHUwbGRZUDVaR2tza2g2ek5pc04%3D; _ga=GA1.2.1177865801.1526852176; _gid=GA1.2.1482933523.1526852176; cookieconsent_dismissed=true; _gat_UA-58591210-1=1', 'SID=JAa1AsrxAXNsD5W-vCFrpqOLUugq9MSCuGZE51Uvj2pH1jvvfJVhZcT-48uDWqfoEs1Gkg.; HSID=AqF7mQzchnpHRAZ5R; SSID=AVwjeJbzhzH8Gu9wm; APISID=UJm2930JU780cmtg/ANYE-hWQ5kr9arUfp; SAPISID=elEPoq5MOa5h09Hc/AyIlmGaM5y51moQhn; 1P_JAR=2018-5-20-21; NID=130=iHpXkMBagoyDXd8KPEKABWt5QE4Pq8_2gFbwck94y_ywhZ62o_-nXfp8FRy4ZfnP5oqroApvdq_H8BJqAQHZa15NWpS5V3VTJuT2pXe_Z_pj_90DtlFIfG5Q5oQOUBJlIv3yPysAT_E2NHfRq_uZBueF7eNTdiK50Nag0wgYLy3pPh2zFdhWiowGo5EGOIYUnfw_2gbJKS50Cmie6rgnGL-VcNQFDtOxXwWrJ6-C0D5T6CE6IWi3SeWU0WWAFRgMKBMfdyjXzR6CeCIEEw; GOOGLE_ABUSE_EXEMPTION=ID=c7cb0d407ea6dcc8:TM=1526853846:C=r:IP=181.56.40.114-:S=APGng0v6j44qf4QdNXEhhUgI4OWsx_Za0A; SIDCC=AEfoLeYJmcI5UaIT675kdMPkCc1rUY6NpFPtqHBLAg-hpF2XJZnTOd0RwhUdeh2x4ek7WPS4gA');


CREATE TABLE `roles` (
  `rol_id` int(11) NOT NULL,
  `label` varchar(255) DEFAULT NULL,
  `referencia` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `roles` (`rol_id`, `label`, `referencia`) VALUES
(1, 'Home', '#URLVista/principal.html'),
(2, 'Ver Publicaciones', '#URLVista/produccionIntelectual.html'),
(3, 'Verificar Publicaciones', '#URLVista/verificarPublicaciones.html'),
(4, 'Carga Fuentes Externas', '#URLVista/actualizarinformacion.html'),
(5, 'Registro Manual', '#URLVista/registroPublicacion.html'),
(6, 'Filtrar Publicaciones', '#URLVista/buscarPublicacion.html'),
(7, 'Estadísticas', '#URLVista/estadisticas.html'),
(8, 'Nube de palabras', '#URLVista/nube.html');


CREATE TABLE `roles_usuarios` (
  `user_id` int(11) NOT NULL,
  `rol_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `sesiones_usuarios` (
  `user_correo` varchar(255) NOT NULL,
  `user_token` varchar(255) DEFAULT NULL,
  `fecha_create` datetime DEFAULT NULL,
  `fecha_last_acces` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE `AreasActuacion`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `IdPersona` (`IdPersona`);

--
-- Indices de la tabla `Departamento`
--
ALTER TABLE `Departamento`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `IdFacultad` (`IdFacultad`);

--
-- Indices de la tabla `Evento`
--
ALTER TABLE `Evento`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `IdPublicacion` (`IdPublicacion`);

--
-- Indices de la tabla `Evento_Persona`
--
ALTER TABLE `Evento_Persona`
  ADD KEY `Evento_Profesor_ibfk_1` (`IdEvento`),
  ADD KEY `IdPersona` (`IdPersona`);

--
-- Indices de la tabla `Facultad`
--
ALTER TABLE `Facultad`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `FormacionAcademica`
--
ALTER TABLE `FormacionAcademica`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `IdPersona` (`IdPersona`);

--
-- Indices de la tabla `Idioma_Persona`
--
ALTER TABLE `Idioma_Persona`
  ADD KEY `IdIdioma` (`IdIdioma`),
  ADD KEY `Idioma_Persona_ibfk_2` (`IdPersona`);

--
-- Indices de la tabla `Idiomas`
--
ALTER TABLE `Idiomas`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `Medallas`
--
ALTER TABLE `Medallas`
  ADD KEY `IdPersona` (`IdPersona`);

--
-- Indices de la tabla `Persona`
--
ALTER TABLE `Persona`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `Persona_Publicacion`
--
ALTER TABLE `Persona_Publicacion`
  ADD KEY `IdPersona` (`IdPersona`),
  ADD KEY `IdPublicacion` (`IdPublicacion`);

--
-- Indices de la tabla `Profesor`
--
ALTER TABLE `Profesor`
  ADD KEY `IdDepartamento` (`IdDepartamento`),
  ADD KEY `IdPersona` (`IdPersona`);

--
-- Indices de la tabla `Publicacion`
--
ALTER TABLE `Publicacion`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `PublicacionDudosa`
--
ALTER TABLE `PublicacionDudosa`
  ADD PRIMARY KEY (`idPublicacionDudosa`),
  ADD KEY `ID` (`ID`);

--
-- Indices de la tabla `Publicacion_Evento`
--
ALTER TABLE `Publicacion_Evento`
  ADD KEY `IdEvento` (`IdEvento`),
  ADD KEY `IdPublicacion` (`IdPublicacion`);

--
-- Indices de la tabla `PuntosPublicacion`
--
ALTER TABLE `PuntosPublicacion`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `datos_usuarios`
--
ALTER TABLE `datos_usuarios`
  ADD PRIMARY KEY (`id`,`user_correo`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`rol_id`);

--
-- Indices de la tabla `sesiones_usuarios`
--
ALTER TABLE `sesiones_usuarios`
  ADD PRIMARY KEY (`user_correo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `AreasActuacion`
--
ALTER TABLE `AreasActuacion`
  MODIFY `ID` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT de la tabla `Departamento`
--
ALTER TABLE `Departamento`
  MODIFY `ID` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT de la tabla `Evento`
--
ALTER TABLE `Evento`
  MODIFY `ID` int(3) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT de la tabla `Facultad`
--
ALTER TABLE `Facultad`
  MODIFY `ID` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT de la tabla `FormacionAcademica`
--
ALTER TABLE `FormacionAcademica`
  MODIFY `ID` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT de la tabla `Idiomas`
--
ALTER TABLE `Idiomas`
  MODIFY `ID` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT de la tabla `Persona`
--
ALTER TABLE `Persona`
  MODIFY `ID` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=515;
--
-- AUTO_INCREMENT de la tabla `Publicacion`
--
ALTER TABLE `Publicacion`
  MODIFY `ID` int(6) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1597;
--
-- AUTO_INCREMENT de la tabla `PublicacionDudosa`
--
ALTER TABLE `PublicacionDudosa`
  MODIFY `idPublicacionDudosa` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=184;
--
-- AUTO_INCREMENT de la tabla `PuntosPublicacion`
--
ALTER TABLE `PuntosPublicacion`
  MODIFY `ID` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=141;
--
-- AUTO_INCREMENT de la tabla `datos_usuarios`
--
ALTER TABLE `datos_usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=815;
--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `rol_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;
--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `AreasActuacion`
--
ALTER TABLE `AreasActuacion`
  ADD CONSTRAINT `AreasActuacion_ibfk_1` FOREIGN KEY (`IdPersona`) REFERENCES `Persona` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `Departamento`
--
ALTER TABLE `Departamento`
  ADD CONSTRAINT `Departamento_ibfk_1` FOREIGN KEY (`IdFacultad`) REFERENCES `Facultad` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `Evento`
--
ALTER TABLE `Evento`
  ADD CONSTRAINT `Evento_ibfk_1` FOREIGN KEY (`IdPublicacion`) REFERENCES `Publicacion` (`ID`);

--
-- Filtros para la tabla `Evento_Persona`
--
ALTER TABLE `Evento_Persona`
  ADD CONSTRAINT `Evento_Persona_ibfk_2` FOREIGN KEY (`IdPersona`) REFERENCES `Evento` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Evento_Persona_ibfk_1` FOREIGN KEY (`IdEvento`) REFERENCES `Evento` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `FormacionAcademica`
--
ALTER TABLE `FormacionAcademica`
  ADD CONSTRAINT `FormacionAcademica_ibfk_1` FOREIGN KEY (`IdPersona`) REFERENCES `Persona` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `Idioma_Persona`
--
ALTER TABLE `Idioma_Persona`
  ADD CONSTRAINT `Idioma_Persona_ibfk_2` FOREIGN KEY (`IdPersona`) REFERENCES `Persona` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Idioma_Persona_ibfk_1` FOREIGN KEY (`IdIdioma`) REFERENCES `Idiomas` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `Medallas`
--
ALTER TABLE `Medallas`
  ADD CONSTRAINT `Medallas_ibfk_1` FOREIGN KEY (`IdPersona`) REFERENCES `Persona` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `Persona_Publicacion`
--
ALTER TABLE `Persona_Publicacion`
  ADD CONSTRAINT `Persona_Publicacion_ibfk_1` FOREIGN KEY (`IdPersona`) REFERENCES `Persona` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Persona_Publicacion_ibfk_2` FOREIGN KEY (`IdPublicacion`) REFERENCES `Publicacion` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `Profesor`
--
ALTER TABLE `Profesor`
  ADD CONSTRAINT `Profesor_ibfk_1` FOREIGN KEY (`IdDepartamento`) REFERENCES `Departamento` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Profesor_ibfk_2` FOREIGN KEY (`IdPersona`) REFERENCES `Persona` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `PublicacionDudosa`
--
ALTER TABLE `PublicacionDudosa`
  ADD CONSTRAINT `PublicacionDudosa_ibfk_1` FOREIGN KEY (`ID`) REFERENCES `Publicacion` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `Publicacion_Evento`
--
ALTER TABLE `Publicacion_Evento`
  ADD CONSTRAINT `Publicacion_Evento_ibfk_1` FOREIGN KEY (`IdEvento`) REFERENCES `Evento` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `Publicacion_Evento_ibfk_2` FOREIGN KEY (`IdPublicacion`) REFERENCES `Publicacion` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;
