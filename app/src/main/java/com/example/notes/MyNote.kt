class MyNote(val title: String, val textContent: String, val id: Int) {
    constructor(title: String, textContent: String) : this(title, textContent, 0) {
    }
}
