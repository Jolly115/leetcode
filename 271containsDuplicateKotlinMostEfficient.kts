class Solution {
        fun containsDuplicate(nums:IntArray):Boolean {
                var seen = HashSet<Int>()
                for (n in nums){
                    if (n in seen){
                        return true
                    }
                    seen.add(n)
                }

                return false
        }
}

fun main(){
    println("Running Kotlin script...")
    val sol = Solution()
    println(sol.containsDuplicate(intArrayOf(1,2,3,4,1)))
}

main()
