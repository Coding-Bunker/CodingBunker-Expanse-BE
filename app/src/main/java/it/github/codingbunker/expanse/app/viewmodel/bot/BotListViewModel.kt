package it.github.codingbunker.expanse.app.viewmodel.bot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.github.codingbunker.tbs.common.model.BotDTO
import it.github.codingbunker.tbs.common.repository.BotManagementRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BotListViewModel(
    private val botRepository: BotManagementRepository
) : ViewModel() {

    private val mUiState = MutableStateFlow(BotListUI())
    val uiState: StateFlow<BotListUI> = mUiState

    init {
        getBotList()
    }

    fun getBotList(refresh: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO + SupervisorJob()) {
            try {
                val list = botRepository.getAllBots(refresh)
                mUiState.update {
                    it.copy(botList = list)
                }
            } catch (ex: Exception) {

            }
        }
    }

    data class BotListUI(
        val loading: Boolean = true,
        val botList: Map<String, BotDTO> = emptyMap()
    )
}