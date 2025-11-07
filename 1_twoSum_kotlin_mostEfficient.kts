
fun twoSum(nums:IntArray, target:Int) : IntArray {
    val map = mutableMapOf<Int,Int>()
    
    for (i in nums.indices)
    {
        var complement = target-nums[i]
        if(map.containsKey(complement))
        	return intArrayOf(i,map[complement]!!)
        map[nums[i]] = i
    }
    return intArrayOf(2,3)
}

fun main() {
    val arr = intArrayOf(7,5,3,4,5)
    print(twoSum(arr,9).joinToString(","))
}


/*class Solution {
    fun twoSum(nums: IntArray, target: Int): IntArray {
        val map = mutableMapOf<Int, Int>()

        for (i in nums.indices) {
            val complement = target - nums[i]
	    println(complement)
            if (map.containsKey(complement)) {
                return intArrayOf(map[complement]!!, i)
            }
            map[nums[i]] = i
	    println(map)
        }

        return intArrayOf() // Return empty if no solution found
    }
}

fun main() {
    val sol = Solution()
    val result = sol.twoSum(intArrayOf(1, 2, 3, 4, 5), 9)
    print(result)
    println(result.joinToString(", "))  // Output: 3, 4
}
*/
