package com.example.level_up_gamer_app.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.level_up_gamer_app.model.CarritoItem

@Composable
fun CarritoItemRow(
    item: CarritoItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF1EAF8)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // NOMBRE + ICONO ELIMINAR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = item.producto.nombre, fontSize = 18.sp)

                IconButton(
                    onClick = onDelete,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // PRECIO
            Text(
                text = "CLP ${item.producto.precio}",
                fontSize = 16.sp,
                color = Color(0xFF6A1B9A)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // CONTROLES DE CANTIDAD
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                // RESTAR
                OutlinedButton(
                    onClick = onDecrement,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(36.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("-", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(text = "${item.cantidad}", fontSize = 18.sp)

                Spacer(modifier = Modifier.width(16.dp))

                // SUMAR
                OutlinedButton(
                    onClick = onIncrement,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.size(36.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("+", fontSize = 20.sp)
                }

                Spacer(modifier = Modifier.weight(1f))

                // SUBTOTAL
                Text(
                    text = "Subtotal: CLP ${item.producto.precio * item.cantidad}",
                    fontSize = 14.sp
                )
            }
        }
    }
}
