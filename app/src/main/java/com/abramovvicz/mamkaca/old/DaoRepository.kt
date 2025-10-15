package com.abramovvicz.mamkaca.old

class DaoRepository(private val dao: QueryDao) {
    suspend fun getAllQueries(): List<QueryEntity>{
        return dao.getAll();
    }
}