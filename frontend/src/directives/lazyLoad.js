const LazyLoad = {
    mounted(el, binding) {
        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = new Image()
                    img.src = binding.value
                    img.onload = () => {
                        el.src = binding.value
                        el.classList.add('loaded')
                    }
                    img.onerror = () => {
                        el.classList.add('error')
                    }
                    observer.unobserve(el)
                }
            })
        }, {
            rootMargin: '0px 0px 100px 0px'
        })

        el._observer = observer
        observer.observe(el)
    },
    unmounted(el) {
        if (el._observer) {
            el._observer.unobserve(el)
        }
    }
}

export default LazyLoad
