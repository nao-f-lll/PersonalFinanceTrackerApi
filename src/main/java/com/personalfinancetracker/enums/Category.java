package com.personalfinancetracker.enums;

import lombok.Getter;

import java.util.List;

@Getter
public enum Category {
    VIVIENDA("Vivienda", "Gastos", "Gastos relacionados con el hogar",
            List.of("Alquiler/Hipoteca", "Servicios (agua, electricidad, gas)", "Mantenimiento y reparaciones")),

    TRANSPORTE("Transporte", "Gastos", "Costos asociados con el transporte personal y público",
            List.of("Combustible", "Mantenimiento del vehículo", "Transporte público", "Seguros del vehículo")),

    ALIMENTACION("Alimentación", "Gastos", "Gastos en comida y bebidas",
            List.of("Comestibles", "Restaurantes", "Cafeterías")),

    SALUD("Salud", "Gastos", "Gastos médicos y de bienestar",
            List.of("Seguro médico", "Medicamentos", "Consultas médicas")),

    EDUCACION("Educación", "Gastos", "Inversiones en educación y formación",
            List.of("Matrícula", "Libros y materiales", "Cursos y seminarios")),

    ENTRETENIMIENTO("Entretenimiento", "Gastos", "Gastos en ocio y recreación",
            List.of("Cine/Teatro", "Suscripciones (Netflix, Spotify...)", "Actividades recreativas")),

    ROPA("Ropa", "Gastos", "Compras de vestimenta y accesorios",
            List.of("Ropa y calzado", "Accesorios")),

    SEGUROS("Seguros", "Gastos", "Pagos de pólizas de seguro",
            List.of("Seguro de vida", "Seguro del hogar")),

    AHORROS_INVERSIONES("Ahorros e Inversiones", "Gastos", "Dinero destinado a ahorros e inversiones",
            List.of("Ahorros", "Inversiones", "Fondos de emergencia")),

    DEUDAS("Deudas", "Gastos", "Pagos relacionados con deudas y créditos",
            List.of("Pagos de préstamos", "Tarjetas de crédito")),

    MISCELANEOS("Misceláneos", "Gastos", "Gastos no categorizados o imprevistos",
            List.of("Regalos", "Donaciones", "Gastos imprevistos")),

    SALARIO("Salario", "Ingresos", "Ingresos provenientes del empleo",
            List.of("Salario principal", "Bonificaciones")),

    INVERSIONES("Ingresos por Inversiones", "Ingresos", "Ganancias derivadas de inversiones",
            List.of("Dividendos", "Intereses")),

    ALQUILER("Ingresos por Alquiler", "Ingresos", "Ingresos obtenidos por alquiler de propiedades",
            List.of("Alquileres de propiedades")),

    VENTAS("Ventas", "Ingresos", "Ingresos por venta de productos o servicios",
            List.of("Venta de bienes", "Ventas freelance")),

    REGALOS_DONACIONES("Regalos y Donaciones", "Ingresos", "Dinero recibido en forma de regalos o donaciones",
            List.of("Regalos monetarios", "Donaciones recibidas")),

    REEMBOLSOS("Reembolsos", "Ingresos", "Dinero devuelto por pagos previos",
            List.of("Reembolsos de impuestos", "Reembolsos de seguros")),

    OTROS("Otros Ingresos", "Ingresos", "Ingresos diversos que no entran en otras categorías",
            List.of("Premios", "Loterías", "Ingresos varios"));


    private final String name;
    private final List<String> subCategories;
    private final String type;
    private final String description;

    Category(String name, String type, String description, List<String> subCategories) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.subCategories = subCategories;
    }
}