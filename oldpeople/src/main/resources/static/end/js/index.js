window.addEventListener('load', function () {
    var focus = document.querySelector('.focus');
    var arrowl = document.querySelector('.arrow-l');
    var arrowr = document.querySelector('.arrow-r');
    var ul = focus.querySelector('ul');
    var ol = focus.querySelector('.circle');
    var focusWidth = focus.offsetWidth;
    var num = 0;
    var circle = 0;
    var timer = null;

    // 鼠标经过focus显示隐藏左右按钮
    focus.addEventListener('mouseover', function () {
        arrowl.style.display = "block";
        arrowr.style.display = "block";
        clearInterval(timer);
        timer = null; // 清除定时器变量
    });

    focus.addEventListener('mouseout', function () {
        arrowl.style.display = "none";
        arrowr.style.display = "none";
        timer = setInterval(function () {
            arrowr.click();
        }, 2000);
    });

    // 动态生成小圆圈
    for (var i = 0; i < ul.children.length; i++) {
        var li = document.createElement('li');
        li.setAttribute('index', i);
        ol.appendChild(li);
    }
    ol.children[0].className = 'current';

    ol.addEventListener('click', function (e) {
        for (var i = 0; i < ol.children.length; i++) {
            ol.children[i].className = '';
        }
        e.target.className = 'current';
        var index = e.target.getAttribute('index');
        num = index;
        circle = index;
        animate(ul, -index * focusWidth);
    });

    var first = ul.children[0].cloneNode(true);
    ul.appendChild(first);

    arrowr.addEventListener('click', function () {
        if (num == ol.children.length) {
            ul.style.left = 0;
            num = 0;
        }
        num++;
        circle++;
        if (circle == ol.children.length) {
            circle = 0;
        }
        circleChange();
        animate(ul, -num * focusWidth);
    });

    arrowl.addEventListener('click', function () {
        if (num == 0) {
            num = ol.children.length;
            ul.style.left = -num * focusWidth + 'px';
        }
        num--;
        circle--;
        if (circle < 0) {
            circle = ol.children.length - 1;
        }
        circleChange();
        animate(ul, -num * focusWidth);
    });

    function circleChange() {
        for (var i = 0; i < ol.children.length; i++) {
            ol.children[i].className = '';
        }
        ol.children[circle].className = 'current';
    }

    timer = setInterval(function () {
        arrowr.click();
    }, 2000);

});