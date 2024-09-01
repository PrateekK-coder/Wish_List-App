package com.example.cart

import androidx.annotation.Nullable
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ShoppingItem(val id:Int,
                        var name:String,
                        var quant:Int,
                        var isEditing:Boolean=false
)
@Composable
fun shoppingItems() {
    var sItem by remember {
        mutableStateOf(listOf<ShoppingItem>())
    }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember {
        mutableStateOf("")
    }
    var itemQuantity by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier.fillMaxSize(),

      verticalArrangement =  Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Button(
            onClick = { showDialog = true },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor =  Color(0xFF7695FF),
                contentColor = Color.White

            )

            ) {
            Text(
                text = "Add Item",

            )

        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sItem) {
              item ->if(item.isEditing){
                  ShoppingItemEditor(item = item, onEditComplete = {
                    editName,editQuant->sItem=sItem.map { it.copy(isEditing = false) }
                            var editedItem=sItem.find { it.id==item.id }
                      editedItem?.let {
                          it.name=editName
                          it.quant=editQuant

                      }
                  })
                
            }else{ 
                ShoppingListItem(item = item, onEditClick = {
                    sItem=sItem.map { it.copy(isEditing = it.id==item.id) }
                },
                    onDeleteClick = {
                        sItem=sItem-item
                    })
            }

            }
        }

    }
    if (showDialog == true) {
        AlertDialog(onDismissRequest = { showDialog = false },
           
            
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {

                            if (itemName.isNotBlank()) {
                                var newItem = ShoppingItem(
                                    id = sItem.size + 1,
                                    name = itemName,
                                    quant = itemQuantity.toInt(),
                                )
                                sItem = sItem + newItem
                                showDialog = false
                                itemName = ""
                                itemQuantity = ""
                            }

                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFF7695FF)
                        )
                    ) {
                        Text(

                            text = "Add",
                            style = TextStyle(
                                fontSize = 20.sp,

                                )
                        )


                    }

                    Button(
                        onClick = { showDialog = false },

                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFF7695FF)
                        )
                    ) {
                        Text(
                            text = "Cancel",
                            style = TextStyle(
                                fontSize = 20.sp,

                                )
                        )
                    }

                }
            },
            title = {
                Text(
                    text = "Add Item to List",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            },
            text = {

                Column {
                    OutlinedTextField(
                        value = itemName, onValueChange = { itemName = it }, singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        placeholder = { Text(text = "Enter the item name") },
                    )
                    OutlinedTextField(
                        value = itemQuantity,
                        onValueChange = { itemQuantity = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        placeholder = { Text(text = "Enter the Quantity") },
                    )

                }
            }

        )
    }

}
@Composable
fun ShoppingItemEditor(item: ShoppingItem,onEditComplete:(String,Int)->Unit){
    var editName by remember { mutableStateOf(item.name)}
    var editQuant by remember { mutableStateOf(item.quant.toString())}
    var isEdit by remember {mutableStateOf(item.isEditing)}

Row (modifier = Modifier
    .fillMaxWidth()
    .background(Color.White)
    .padding(8.dp), horizontalArrangement = Arrangement.SpaceEvenly){

}
Column {
    BasicTextField(value = editName,
        onValueChange ={editName=it},
        singleLine = true,
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp))

    BasicTextField(value = editQuant,
        onValueChange ={editQuant=it},
        singleLine = true,
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp))

    Button(onClick = {
        isEdit=false
        onEditComplete(editName,editQuant.toIntOrNull()?:1)

    }) {
        Text(text = "Save",
            )
    }
}


}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                border = BorderStroke(2.dp, Color(0xFF7695FF)),
                shape = RoundedCornerShape(20)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically // Align items vertically in the center
    ) {
        // Display the quantity and name with proportional spacing
        Text(
            text = item.name,
            modifier = Modifier.padding(8.dp)

        )
        Text(
            text = "QTY: "+item.quant.toString(),
            modifier = Modifier.padding(8.dp)


        )
        Row(modifier = Modifier.padding(8.dp)){ 
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
                
            }
            
        }
    }
