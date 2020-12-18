package ufobat.aoc2020

import kotlin.math.absoluteValue

data class ActiveCube(val x: Int, val y: Int, val z: Int, val w: Int = 0) {
//    fun isNeighbor(other: ActiveCube): Boolean {
//        val dx = (x - other.x).absoluteValue
//        val dy = (y - other.y).absoluteValue
//        val dz = (z - other.z).absoluteValue
//        return if (dx == 0 && dy == 0 && dz == 0) false
//        else dx <= 1 && dy <= 1 && dz <= 1
//    }

    fun isNeighbor(other: ActiveCube): Boolean {
        val dx = (x - other.x).absoluteValue
        val dy = (y - other.y).absoluteValue
        val dz = (z - other.z).absoluteValue
        val dw = (w - other.w).absoluteValue
        return if (dx == 0 && dy == 0 && dz == 0 && dw == 0) false
        else dx <= 1 && dy <= 1 && dz <= 1 && dw <= 1
    }

    fun neighborCubes() : List<ActiveCube> {
        val neighbors = mutableListOf<ActiveCube>()
        val offsets = listOf(-1, 0, 1)
        offsets.forEach { zo ->
            offsets.forEach { yo ->
                offsets.forEach { xo ->
                    if (!(xo == 0 && yo == 0 && zo == 0)) {
                        neighbors.add(ActiveCube(x + xo, y + yo, z + zo))
                    }
                }
            }
        }
        return neighbors
    }

    fun hyperNeighborCubes() : List<ActiveCube> {
        val neighbors = mutableListOf<ActiveCube>()
        val offsets = listOf(-1, 0, 1)
        offsets.forEach { wo ->
            offsets.forEach { zo ->
                offsets.forEach { yo ->
                    offsets.forEach { xo ->
                        if (!(xo == 0 && yo == 0 && zo == 0)) {
                            neighbors.add(ActiveCube(x + xo, y + yo, z + zo, w + wo))
                        }
                    }
                }
            }
        }
        return neighbors
    }
}
class CubeSpace() {
    val activeCubes = mutableSetOf<ActiveCube>()
    fun activateCube(x: Int, y: Int, z: Int) = activateCube(ActiveCube(x, y, z))
    fun activateCube(c: ActiveCube) = activeCubes.add(c)

    fun count() = activeCubes.size

    /*
     * If a cube is active and exactly 2 or 3 of its neighbors are also active, the cube remains active.
     * Otherwise, the cube becomes inactive.
     *
     * If a cube is inactive but exactly 3 of its neighbors are active, the cube becomes active.
     * Otherwise, the cube remains inactive.
     */
    fun next() :CubeSpace {
        val next = CubeSpace()
        activeCubes.forEach { c ->
            val an = cntActiveNeighbors(c)
            if( an == 2 || an == 3) next.activateCube(c)

            c.neighborCubes().forEach { nc ->
                val anc = cntActiveNeighbors(nc)
                // println("$nc is neighbor with $anc")
                if (anc == 3) next.activateCube(nc)
            }
        }

        return next
    }

    fun nextHyper() :CubeSpace {
        val next = CubeSpace()
        activeCubes.forEach { c ->
            val an = cntActiveNeighbors(c)
            if( an == 2 || an == 3) next.activateCube(c)

            c.hyperNeighborCubes().forEach { nc ->
                val anc = cntActiveNeighbors(nc)
                if (anc == 3) {
                    // println("$nc is neighbor with $anc")
                    next.activateCube(nc)
                }
            }
        }

        return next
    }

    private fun cntActiveNeighbors(c: ActiveCube ) = activeCubes.filter { c.isNeighbor(it) }.count()

}

object Data17 {
    fun getDemoData() = listOf<String>(
            ".#.",
            "..#",
            "###"
    )

    fun getPuzzleData() = listOf<String>(
            ".#.#.#..",
            "..#....#",
            "#####..#",
            "#####..#",
            "#####..#",
            "###..#.#",
            "#..##.##",
            "#.#.####"
    )

}

fun main() {
    val data = Data17.getPuzzleData()
    var cubespace = CubeSpace()
    var hyperspace = CubeSpace()
    val activeCubes = data.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c == '#') {
                cubespace.activateCube(x, y, 0)
                hyperspace.activateCube(x, y, 0)
            }
        }
    }

    for (i in 0 until 7) {
        println("$i -> " + cubespace.count() + " <-> " + hyperspace.count())
        cubespace = cubespace.next()
        hyperspace = hyperspace.nextHyper();
    }
}
