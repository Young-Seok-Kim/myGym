//package com.youngs.mygym.ui.employee.member.room
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.ViewModel
//
//class MemberViewModel(
//    private val MemberDao: MemberDao
//): ViewModel() {
//    val allItems: LiveData<List<MemberEntity>> = MemberDao.getAllMembers().asLiveData() // 이 줄을 추가
//
//    private fun insertItem(item: Item) {
//        viewModelScope.launch {
//            MemberDao.insert(item)
//        }
//    }
//
//    private fun getNewItemEntry(itemName: String, itemPrice: String, itemCount: String): Item =
//        Item(
//            itemName = itemName,
//            itemPrice = itemPrice.toDouble(),
//            quantityInStock = itemCount.toInt()
//        )
//
//    fun addNewItem(itemName: String, itemPrice: String, itemCount: String) {
//        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
//        insertItem(newItem)
//    }
//
//    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
//        if (itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank()) {
//            return false
//        }
//        return true
//    }
//}
//
//class InventoryViewModelFactory(
//    private val MemberDao: MemberDao
//): ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        require(modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
//            throw IllegalArgumentException("Unknown ViewModel class")
//        }
//
//        @Suppress("UNCHECKED_CAST")
//        return InventoryViewModel(MemberDao) as T
//    }
//}