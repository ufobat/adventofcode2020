package ufobat.aoc2020

import java.util.*
import kotlin.math.sqrt
import kotlin.streams.toList

object Data20 {
    fun getPuzzleInput() = this::class.java.getResourceAsStream("/day20_puzzle_input.txt")
            .bufferedReader()
            .lines()
            .toList()

    fun getDemoInput() = this::class.java.getResourceAsStream("/day20_demo_input.txt")
            .bufferedReader()
            .lines()
            .toList()

}

class TileGrid(private val size: Int,
               private val grid: List<Tile> = listOf()) {

    fun complete(): Boolean { return grid.size == size * size }
    private fun getWestOf(curPos: Int)= if (curPos % size == 0) null else grid[curPos - 1]
    private fun getNorthOf(curPos: Int) = if (curPos / size == 0) null else grid[curPos - size]

    fun add(t: Tile): TileGrid? {
        val curPos = grid.size

        getNorthOf(curPos)?.let { if (it.south() != t.north()) return null }
        getWestOf(curPos)?.let { if (it.east() != t.west()) return null }

        return TileGrid(size, grid.toMutableList().apply { add(t) })
    }

    private fun tileAt(row: Int, col: Int) = grid[row * size + col]
    override fun toString(): String {
        val b = StringBuilder()
        for (row in 0 until size) {
            for (col in 0 until size) {
                b.append(tileAt(row, col).id)
                b.append(" ")
            }
            b.append("\n")
        }
        return b.toString()
    }

    fun toTile() : Tile {
        val lines = Array(8 * size ) { StringBuilder() }
        // from left to right
        for (col in 0 until size) {
            for (row in 0 until size) {
                val t = tileAt(row, col)
                val x= t.pixels.subList(1, t.pixels.size-1)
                x.forEachIndexed { index, s ->
                    val offset = index + row * 8
                    lines[offset].append( s.substring(1, s.length-1) )
                }
            }
        }
        return Tile(1337, lines.map { it.toString() })
    }

    fun edgeMultiply(): Long {
        // 0, size-1,
        return tileAt(0,0).id *
        tileAt(0, size -1).id *
        tileAt(size-1, 0).id *
        tileAt(size-1, size-1).id
    }
}

fun seaMonster(): Tile {
    val pixels = listOf(
            "                  # ",
            "#    ##    ##    ###",
            " #  #  #  #  #  #   ",
    )
    return Tile(666, pixels)
}

fun main() {
    val data = Data20.getPuzzleInput()
    val tiles = parseTiles(data)

    val grid = TileGrid(sqrt(tiles.size.toDouble()).toInt())
    val result = recCompGrid(tiles, grid)!!

    println( result.edgeMultiply() )

    val monster = seaMonster()
    val resultTile = result.toTile()
    val hashCount = resultTile.count('#')
    val monsterHashCount = monster.count('#')

    resultTile.variants().forEach { tile ->
        val noOfMonsters = tile.countMatching(monster)
        if (noOfMonsters != 0) {
            println(hashCount - noOfMonsters * monsterHashCount)
        }
    }
}

fun recCompGrid(tiles: List<Tile>, grid: TileGrid): TileGrid? {
    for (idx in tiles.indices) {
        val tile = tiles[idx]
        val remain = tiles.filterIndexed { i, _ -> idx != i }
        tile.variants().forEach { tv ->
            val newGrid = grid.add(tv)
            if (newGrid != null) {
                if (newGrid.complete()) return newGrid
                recCompGrid(remain, newGrid)?.let { return it }
            }
        }
    }
    return null
}

data class Tile(val id: Long,
                val pixels: List<String>,
                val orientation: Int = 0,
                val flipMode: Int = 0){

    fun info(): String = "$id ($orientation, $flipMode)"
    fun pattern() = pixels.joinToString("\n")
    fun width() = pixels.first().length
    fun height() = pixels.size

    private fun orientatedAs(o: Int, f: Int): Tile {
        var modifiedPixels =
                if (f != 0) pixels.map { it.reversed() }
                else pixels

        for (i in 0 until o) modifiedPixels = rotateRight(modifiedPixels)
        return Tile(id, modifiedPixels, o, f)
    }

    private fun rotateRight(pixels: List<String>): List<String> {
        val strings= Array(pixels.size) { StringBuilder() }
        pixels.reversed().forEach {
            it.toCharArray().forEachIndexed { index, c ->
                strings[index].append(c)
            }
        }
        return strings.map { it.toString() }.toList()
    }


    fun north() = pixels[0]
    fun south() = pixels.last()
    fun east(): String {
        val b = StringBuilder()
        pixels.forEach { b.append( it.last() ) }
        return b.toString()
    }
    fun west(): String {
        val b = StringBuilder()
        pixels.forEach { b.append( it.first()) }
        return b.toString()
    }

    fun variants() = sequence {
        for (o in 0..3)
            for (f in 0..1)
                yield( this@Tile.orientatedAs(o, f) )
    }

    fun countMatching(monster: Tile): Int {
        var cnt = 0;
        for (c in 0 until this.width() - monster.width()) {
            for(r in 0 until this.height() - monster.height()) {
                val subtile = subTile(c, r, monster.width(), monster.height())
                if (monster.matches(subtile)) cnt++
            }
        }
        return cnt;
    }

    fun count(char: Char) = pixels.map {
        it.toCharArray().filter { it == char }.count().toLong()
    }.sum()

    private fun subTile(c: Int, r: Int, width: Int, height: Int) = Tile(
            id,
            pixels.subList(r, r + height).map { it.substring(c, c+width) }
    )

    private fun matches(other: Tile, char: Char = '#'): Boolean {
        if (this.pixels.size != other.pixels.size) return false

        this.pixels.zip(other.pixels).forEach { (pl,ol) ->
            if (pl.length != ol.length) return false

            pl.toCharArray().zip( ol.toCharArray() ).forEach { (pc, oc) ->
                if (pc == char && oc != pc) return false
            }
        }

        return true
    }
}

fun parseTiles(data: List<String>): List<Tile> {
    val tiles = mutableListOf<Tile>()
    var id: String? = null
    var pixels = mutableListOf<String>()
    data.forEach {
        when {
            id == null      -> id = it
            it.isNotEmpty() -> pixels.add(it)
            else            -> {
                val numId = id!!.substring("Tile ".length, id!!.length - 1).toLong()
                tiles.add(Tile(numId, pixels))
                id = null; pixels = mutableListOf() // reset
            }
        }
    }
    return tiles
}
