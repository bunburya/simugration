package eu.bunburya.simugration

abstract class BaseSimugrationError(msg: String): Exception(msg)

class BadCellError(msg: String): BaseSimugrationError(msg)