import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagenMatriz {
    private int[][][] matrizPixeles; // [alto][ancho][4] -> A, R, G, B
    private int ancho;
    private int alto;

    // Constructor: lee la imagen desde el classpath y llena la matriz
    public ImagenMatriz(String ruta) throws IOException {
        BufferedImage imagen = ImageIO.read(
            ImagenMatriz.class.getResourceAsStream(ruta)
        );

        if (imagen == null) {
            throw new IOException("No se pudo cargar la imagen: " + ruta);
        }

        this.ancho = imagen.getWidth();
        this.alto = imagen.getHeight();
        matrizPixeles = new int[alto][ancho][4];

        llenarMatriz(imagen);
    }

    public void llenarMatriz(BufferedImage imagen) {
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = imagen.getRGB(x, y);
                matrizPixeles[y][x][0] = (pixel >> 24) & 0xff; // A
                matrizPixeles[y][x][1] = (pixel >> 16) & 0xff; // R
                matrizPixeles[y][x][2] = (pixel >> 8) & 0xff;  // G
                matrizPixeles[y][x][3] = pixel & 0xff;         // B
            }
        }
    }

    // Getters
    public int[][][] getMatrizPixeles() {
        return matrizPixeles;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

     public BufferedImage toBufferedImage() {
        BufferedImage nueva = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int a = matrizPixeles[y][x][0];
                int r = matrizPixeles[y][x][1];
                int g = matrizPixeles[y][x][2];
                int b = matrizPixeles[y][x][3];

                // Reconstruir entero ARGB
                int argb = (a << 24) | (r << 16) | (g << 8) | b;

                nueva.setRGB(x, y, argb);
            }
        }
        return nueva;
    }

    public void guardarImagen(String rutaSalida) throws IOException {
        BufferedImage nueva = toBufferedImage();
        File archivoSalida = new File(rutaSalida);

        // Crea directorios si no existen
        archivoSalida.getParentFile().mkdirs();

        ImageIO.write(nueva, "png", archivoSalida);
    }

    // Prueba
    public static void main(String[] args) {
        try {
            // Usa la misma ruta que en tu App
            ImagenMatriz img = new ImagenMatriz("/imagenes/imagen.png");

            img.matrizPixeles[10][10][1] = 255; // Rojo al máximo
            img.matrizPixeles[10][10][2] = 0;   // Verde en 0
            img.matrizPixeles[10][10][3] = 0;   // Azul en 0

            // Guardar nueva imagen
            img.guardarImagen("salida/imagen_modificada.png");

            System.out.println("Imagen modificada y guardada.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}