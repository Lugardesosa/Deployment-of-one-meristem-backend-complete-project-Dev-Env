# templates/_helpers.tpl
{{/* Generate a name using the release name */}}
{{- define "config-server.fullname" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}

{{- define "config-server.name" -}}
{{ .Release.Name }}-{{ .Values.environment }}
{{- end }}


