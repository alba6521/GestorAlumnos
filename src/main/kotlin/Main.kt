import java.util.Locale

// estructura de datos
data class Alumno(
    val nombre: String,
    val apellidos: String,
    val nota: Double
)

const val NOTA_APROBADO = 5.0

fun main() {
    val alumnos = mutableListOf<Alumno>()
    var salir = false

    // menú repetido hasta elegir salir
    while (!salir) {
        mostrarMenu()
        when (readLine()?.trim()) {
            "1" -> registrarAlumno(alumnos)
            "2" -> listarAlumnos(alumnos)
            "3" -> {
                if (alumnos.isEmpty()) {
                    println("No hay alumnos registrados. No se puede calcular la media.")
                } else {
                    println("La media del grupo es: ${formatear(calcularMedia(alumnos))}")
                }
            }
            "4" -> mostrarAprobados(alumnos)
            "5" -> mostrarSuspensos(alumnos)
            "6" -> buscarAlumno(alumnos)
            "7" -> eliminarAlumno(alumnos)
            "8" -> {
                println("Saliendo del programa. ¡Hasta pronto!")
                salir = true
            }
            else -> println("Opción no válida. Introduce un número del 1 al 8.")
        }
        println()
    }
}

fun mostrarMenu() {
    println("===== GESTOR DE ALUMNOS =====")
    println("1. Registrar alumno")
    println("2. Listar alumnos")
    println("3. Calcular media del grupo")
    println("4. Mostrar aprobados")
    println("5. Mostrar suspensos")
    println("6. Buscar alumno")
    println("7. Eliminar alumno")
    println("8. Salir")
    print("Seleccione una opción: ")
}

// Utilidades
fun formatear(valor: Double): String =
    String.format(Locale.US, "%.2f", valor).trimEnd('0').trimEnd('.')

fun describir(alumno: Alumno): String =
    "${alumno.nombre} ${alumno.apellidos} - Nota: ${formatear(alumno.nota)}"

fun pedirTexto(mensaje: String): String {
    while (true) {
        print(mensaje)
        val texto = readLine()?.trim().orEmpty()
        if (texto.isNotEmpty()) return texto
        println("El campo no puede estar vacío.")
    }
}

fun pedirNota(mensaje: String): Double {
    while (true) {
        print(mensaje)
        val nota = readLine()?.trim()?.replace(',', '.')?.toDoubleOrNull()
        when {
            nota == null -> println("Debes introducir un valor numérico.")
            nota < 0.0 || nota > 10.0 -> println("La nota debe estar entre 0 y 10.")
            else -> return nota
        }
    }
}

// registro
fun registrarAlumno(alumnos: MutableList<Alumno>) {
    val nombre = pedirTexto("Introduce el nombre: ")
    val apellidos = pedirTexto("Introduce los apellidos: ")
    val nota = pedirNota("Introduce la nota final: ")
    alumnos.add(Alumno(nombre, apellidos, nota))
    println("Alumno registrado correctamente.")
}

// listado
fun listarAlumnos(alumnos: List<Alumno>) {
    if (alumnos.isEmpty()) {
        println("No hay alumnos registrados.")
        return
    }
    alumnos.forEachIndexed { indice, alumno ->
        println("${indice + 1}. ${describir(alumno)}")
    }
}

// media
fun calcularMedia(alumnos: List<Alumno>): Double =
    if (alumnos.isEmpty()) 0.0 else alumnos.map { it.nota }.average()

// aprobados y suspensos
fun mostrarAprobados(alumnos: List<Alumno>) {
    println("===== ALUMNOS APROBADOS =====")
    val aprobados = alumnos.filter { it.nota >= NOTA_APROBADO }
    if (aprobados.isEmpty()) {
        println("No hay alumnos aprobados.")
    } else {
        aprobados.forEach { println(describir(it)) }
    }
}

fun mostrarSuspensos(alumnos: List<Alumno>) {
    println("===== ALUMNOS SUSPENSOS =====")
    val suspensos = alumnos.filter { it.nota < NOTA_APROBADO }
    if (suspensos.isEmpty()) {
        println("No hay alumnos suspensos.")
    } else {
        suspensos.forEach { println(describir(it)) }
    }
}

// búsqueda (coincidencia parcial)
fun buscarAlumno(alumnos: List<Alumno>) {
    val texto = pedirTexto("Introduce el texto a buscar: ")
    val encontrados = alumnos.filter {
        it.nombre.contains(texto, ignoreCase = true) ||
                it.apellidos.contains(texto, ignoreCase = true)
    }
    if (encontrados.isEmpty()) {
        println("No se han encontrado alumnos con ese criterio.")
    } else {
        encontrados.forEach { println(describir(it)) }
    }
}

// eliminación por posición
fun eliminarAlumno(alumnos: MutableList<Alumno>) {
    if (alumnos.isEmpty()) {
        println("No hay alumnos registrados. No hay nada que eliminar.")
        return
    }
    listarAlumnos(alumnos)
    print("Introduce el número del alumno a eliminar: ")
    val posicion = readLine()?.trim()?.toIntOrNull()
    if (posicion == null || posicion < 1 || posicion > alumnos.size) {
        println("Número no válido. El alumno indicado no existe.")
        return
    }
    alumnos.removeAt(posicion - 1)
    println("Alumno eliminado correctamente.")
}