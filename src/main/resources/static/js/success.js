(function()
{

    const leftArrow = document.querySelector(".icon-arrow-left2")
    const rightArrow = document.querySelector(".icon-arrow-right2")
    const recordsAmount = document.querySelector(".records-amount").getAttribute("href");
    const leftArrowRedirectIndex = leftArrow.getAttribute("href")
    const currentIndex = parseInt(document.querySelector(".current-index").textContent)

    if (leftArrowRedirectIndex == 0)
    {
        // leftArrow.setAttribute("href", "1")
        leftArrow.style = "visibility: hidden;"
    }

    if ((currentIndex * 6) >= recordsAmount)
    {
        // rightArrow.setAttribute("href", currentIndex.toString())
        rightArrow.style = "visibility: hidden;"
    }
})()
