package it.zebco.alm.svn.adapter

interface SvnCommand {
    def execute(List<String> parameters)
}